package com.utn.health.data.repository

import com.utn.health.data.local.HealthDatabase
import com.utn.health.data.local.entity.DailySummaryEntity
import com.utn.health.data.local.entity.StreakEntity
import com.utn.health.data.local.entity.SyncStatusEntity
import com.utn.health.domain.model.ConnectionState
import com.utn.health.domain.model.DailySummary
import com.utn.health.domain.model.HealthConnectSdkStatus
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.MetricType
import com.utn.health.domain.model.StreakState
import com.utn.health.domain.provider.HealthConnectionProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate

class HealthRepositoryImpl(
    private val healthProvider: HealthConnectionProvider,
    private val database: HealthDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : HealthRepository {

    private val dailySummaryDao = database.dailySummaryDao()
    private val streakDao = database.streakDao()
    private val syncStatusDao = database.syncStatusDao()

    private val _connectionState = MutableStateFlow(ConnectionState())
    private val _streakState = MutableStateFlow(StreakState())

    init {
        // Inicializar estado de rachas por defecto si no existe en Room
    }

    override fun getTodaySummary(forceRefresh: Boolean): Flow<DailySummary> = flow {
        val today = LocalDate.now()
        val todayStr = today.toString()

        // 1. Emitir primero desde la caché de Room si existe
        val cached = dailySummaryDao.getSummaryByDateSync(todayStr)
        if (cached != null) {
            emit(cached.toDomainModel(isFromCache = true))
        }

        // 2. Consultar Health Connect si el SDK está disponible
        val sdkStatus = healthProvider.getSdkStatus()
        val granted = healthProvider.getGrantedPermissions()

        if (sdkStatus == HealthConnectSdkStatus.Available && granted.isNotEmpty()) {
            try {
                val liveSummary = healthProvider.readDaySummary(today, granted)
                // Guardar en Room
                dailySummaryDao.insertOrUpdate(liveSummary.toEntity())
                // Actualizar motor de rachas con la distancia
                updateStreakWithDistance(liveSummary.distanceKm ?: 0.0)
                emit(liveSummary.copy(isFromCache = false))
            } catch (e: Exception) {
                if (cached == null) {
                    emit(DailySummary(date = today, isFromCache = true))
                }
            }
        } else if (cached == null) {
            emit(DailySummary(date = today, isFromCache = false))
        }
    }.flowOn(ioDispatcher)

    override fun getHistory(metric: MetricType, days: Int): Flow<HistoryData> = flow {
        val granted = healthProvider.getGrantedPermissions()
        val history = healthProvider.readHistory(metric, days, granted)
        emit(history)
    }.flowOn(ioDispatcher)

    override fun getHeartRateSamples(date: LocalDate): Flow<List<HeartRateSample>> = flow {
        val samples = healthProvider.readHeartRateSamples(date)
        emit(samples)
    }.flowOn(ioDispatcher)

    override fun getConnectionState(): Flow<ConnectionState> = flow {
        refreshConnectionState()
        emit(_connectionState.value)
    }.flowOn(ioDispatcher)

    override fun getStreakState(): Flow<StreakState> {
        val todayStr = LocalDate.now().toString()
        return combine(
            streakDao.getStreak(),
            dailySummaryDao.getSummaryByDate(todayStr)
        ) { streakEntity, summaryEntity ->
            val entity = streakEntity ?: StreakEntity()
            val todayDistanceKm = (summaryEntity?.distanciaMetros ?: 0.0) / 1000.0
            val completed = parseDates(entity.fechasCompletadasCsv)
            val today = LocalDate.now()
            val isMet = todayDistanceKm >= entity.metaKm || completed.contains(today)

            val state = StreakState(
                currentStreak = entity.rachaActual,
                longestStreak = entity.mejorRacha,
                dailyDistanceGoalKm = entity.metaKm,
                todayDistanceKm = todayDistanceKm,
                isGoalCompletedToday = isMet,
                streakFreezesAvailable = entity.congeladoresDisponibles,
                isFrozenToday = entity.estaCongeladaHoy,
                isStreakAtRisk = !isMet && !entity.estaCongeladaHoy && entity.rachaActual > 0,
                isStreakRecoverable = entity.rachaRecuperable,
                recoverableStreakCount = entity.conteoRecuperable,
                lastCompletedDate = entity.ultimaFechaCompletada?.let { LocalDate.parse(it) },
                completedDates = completed,
                userWeightKg = entity.pesoKg,
                userHeightCm = entity.estaturaCm
            )
            _streakState.value = state
            state
        }.distinctUntilChanged().flowOn(ioDispatcher)
    }

    override suspend fun setDailyDistanceGoal(goalKm: Double) = withContext(ioDispatcher) {
        val current = streakDao.getStreakSync() ?: StreakEntity()
        val updated = current.copy(metaKm = goalKm)
        streakDao.insertOrUpdate(updated)
        recalculateStreakStatus(updated)
    }

    override suspend fun activateStreakFreeze(): Boolean = withContext(ioDispatcher) {
        val current = streakDao.getStreakSync() ?: StreakEntity()
        if (current.congeladoresDisponibles > 0 && !current.estaCongeladaHoy) {
            val updated = current.copy(
                congeladoresDisponibles = current.congeladoresDisponibles - 1,
                estaCongeladaHoy = true
            )
            streakDao.insertOrUpdate(updated)
            recalculateStreakStatus(updated)
            true
        } else {
            false
        }
    }

    override suspend fun recoverStreak(): Boolean = withContext(ioDispatcher) {
        val current = streakDao.getStreakSync() ?: StreakEntity()
        if (current.rachaRecuperable && current.conteoRecuperable > 0) {
            val updated = current.copy(
                rachaActual = current.conteoRecuperable,
                rachaRecuperable = false,
                conteoRecuperable = 0
            )
            streakDao.insertOrUpdate(updated)
            recalculateStreakStatus(updated)
            true
        } else {
            false
        }
    }

    override suspend fun saveUserProfile(weightKg: Double, heightCm: Double) = withContext(ioDispatcher) {
        val current = streakDao.getStreakSync() ?: StreakEntity()
        val updated = current.copy(
            pesoKg = weightKg,
            estaturaCm = heightCm
        )
        streakDao.insertOrUpdate(updated)
        recalculateStreakStatus(updated)
    }

    override suspend fun refreshAll() = withContext(ioDispatcher) {
        refreshConnectionState()
        val today = LocalDate.now()
        val granted = healthProvider.getGrantedPermissions()
        if (healthProvider.getSdkStatus() == HealthConnectSdkStatus.Available && granted.isNotEmpty()) {
            val summary = healthProvider.readDaySummary(today, granted)
            dailySummaryDao.insertOrUpdate(summary.toEntity())
            updateStreakWithDistance(summary.distanceKm ?: 0.0)
        }
    }

    private suspend fun refreshConnectionState() {
        val sdkStatus = healthProvider.getSdkStatus()
        val granted = healthProvider.getGrantedPermissions()
        val sources = healthProvider.getDetectedSources()
        val isReady = sdkStatus == HealthConnectSdkStatus.Available && granted.containsAll(healthProvider.requiredPermissions)

        val newState = ConnectionState(
            sdkStatus = sdkStatus,
            requiredPermissions = healthProvider.requiredPermissions,
            grantedPermissions = granted,
            detectedSources = sources,
            lastSyncTime = Instant.now(),
            isReady = isReady
        )
        _connectionState.value = newState

        syncStatusDao.insertOrUpdate(
            SyncStatusEntity(
                permisosConcedidos = newState.hasAllPermissions,
                healthConnectDisponible = sdkStatus == HealthConnectSdkStatus.Available,
                ultimaSincronizacion = newState.lastSyncTime,
                fuentesDetectadas = sources.joinToString(",") { it.packageName }
            )
        )
    }

    private suspend fun updateStreakWithDistance(todayKm: Double) {
        val current = streakDao.getStreakSync() ?: StreakEntity()
        val today = LocalDate.now()
        val todayStr = today.toString()
        val completedSet = parseDates(current.fechasCompletadasCsv).toMutableSet()

        val isGoalMetToday = todayKm >= current.metaKm
        var newCurrentStreak = current.rachaActual
        var newLongestStreak = current.mejorRacha
        var recoverable = current.rachaRecuperable
        var recoverableCount = current.conteoRecuperable

        if (isGoalMetToday) {
            if (!completedSet.contains(today)) {
                completedSet.add(today)
                val yesterday = today.minusDays(1)
                if (completedSet.contains(yesterday) || current.estaCongeladaHoy) {
                    newCurrentStreak += 1
                } else if (newCurrentStreak == 0) {
                    newCurrentStreak = 1
                }
                if (newCurrentStreak > newLongestStreak) {
                    newLongestStreak = newCurrentStreak
                }
                recoverable = false
            }
        } else {
            // Verificar si ayer se perdió la racha
            val yesterday = today.minusDays(1)
            val dayBeforeYesterday = today.minusDays(2)
            if (completedSet.contains(dayBeforeYesterday) && !completedSet.contains(yesterday) && !current.estaCongeladaHoy) {
                if (newCurrentStreak > 0) {
                    // Permitir recuperación durante el día actual
                    recoverable = true
                    recoverableCount = newCurrentStreak
                    newCurrentStreak = 0
                }
            }
        }

        val updated = current.copy(
            rachaActual = newCurrentStreak,
            mejorRacha = newLongestStreak,
            ultimaFechaCompletada = if (isGoalMetToday) todayStr else current.ultimaFechaCompletada,
            fechasCompletadasCsv = completedSet.joinToString(",") { it.toString() },
            rachaRecuperable = recoverable,
            conteoRecuperable = recoverableCount
        )

        streakDao.insertOrUpdate(updated)
        recalculateStreakStatus(updated, todayKm)
    }

    private suspend fun recalculateStreakStatus(entity: StreakEntity, distanceKm: Double? = null) {
        val today = LocalDate.now()
        val completed = parseDates(entity.fechasCompletadasCsv)
        val todayDistance = distanceKm ?: ((dailySummaryDao.getSummaryByDateSync(today.toString())?.distanciaMetros ?: 0.0) / 1000.0)
        val isMet = todayDistance >= entity.metaKm || completed.contains(today)

        _streakState.value = StreakState(
            currentStreak = entity.rachaActual,
            longestStreak = entity.mejorRacha,
            dailyDistanceGoalKm = entity.metaKm,
            todayDistanceKm = todayDistance,
            isGoalCompletedToday = isMet,
            streakFreezesAvailable = entity.congeladoresDisponibles,
            isFrozenToday = entity.estaCongeladaHoy,
            isStreakAtRisk = !isMet && !entity.estaCongeladaHoy && entity.rachaActual > 0,
            isStreakRecoverable = entity.rachaRecuperable,
            recoverableStreakCount = entity.conteoRecuperable,
            lastCompletedDate = entity.ultimaFechaCompletada?.let { LocalDate.parse(it) },
            completedDates = completed,
            userWeightKg = entity.pesoKg,
            userHeightCm = entity.estaturaCm
        )
    }

    private fun parseDates(csv: String): Set<LocalDate> {
        if (csv.isBlank()) return emptySet()
        return csv.split(",")
            .filter { it.isNotBlank() }
            .mapNotNull {
                try { LocalDate.parse(it.trim()) } catch (e: Exception) { null }
            }.toSet()
    }

    private fun DailySummaryEntity.toDomainModel(isFromCache: Boolean): DailySummary {
        return DailySummary(
            date = LocalDate.parse(this.fecha),
            steps = this.pasos,
            distanceMeters = this.distanciaMetros,
            caloriesKcal = this.caloriasKcal,
            heartRateAvg = this.fcPromedio,
            heartRateMin = this.fcMin,
            heartRateMax = this.fcMax,
            sleepMinutes = this.minutosSueno,
            lastSync = this.ultimaSincronizacion,
            isFromCache = isFromCache
        )
    }

    private fun DailySummary.toEntity(): DailySummaryEntity {
        return DailySummaryEntity(
            fecha = this.date.toString(),
            pasos = this.steps,
            distanciaMetros = this.distanceMeters,
            caloriasKcal = this.caloriesKcal,
            fcPromedio = this.heartRateAvg,
            fcMin = this.heartRateMin,
            fcMax = this.heartRateMax,
            minutosSueno = this.sleepMinutes,
            ultimaSincronizacion = this.lastSync
        )
    }

    private fun StreakEntity.toDomainModel(): StreakState {
        val completed = parseDates(this.fechasCompletadasCsv)
        val today = LocalDate.now()
        val isMet = completed.contains(today)

        return StreakState(
            currentStreak = this.rachaActual,
            longestStreak = this.mejorRacha,
            dailyDistanceGoalKm = this.metaKm,
            todayDistanceKm = 0.0,
            isGoalCompletedToday = isMet,
            streakFreezesAvailable = this.congeladoresDisponibles,
            isFrozenToday = this.estaCongeladaHoy,
            isStreakAtRisk = !isMet && !this.estaCongeladaHoy && this.rachaActual > 0,
            isStreakRecoverable = this.rachaRecuperable,
            recoverableStreakCount = this.conteoRecuperable,
            lastCompletedDate = this.ultimaFechaCompletada?.let { LocalDate.parse(it) },
            completedDates = completed,
            userWeightKg = this.pesoKg,
            userHeightCm = this.estaturaCm
        )
    }
}
