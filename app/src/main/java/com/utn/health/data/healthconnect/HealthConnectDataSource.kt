package com.utn.health.data.healthconnect

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.utn.health.domain.model.DailySummary
import com.utn.health.domain.model.HealthConnectSdkStatus
import com.utn.health.domain.model.HealthDeviceSource
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.HistoryPoint
import com.utn.health.domain.model.MetricType
import com.utn.health.domain.provider.HealthConnectionProvider
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.util.Locale

class HealthConnectDataSource(
    private val context: Context
) : HealthConnectionProvider {

    private val appContext = context.applicationContext

    private val client: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(appContext)
    }

    private val stepsPermission = HealthPermission.getReadPermission(StepsRecord::class)
    private val distancePermission = HealthPermission.getReadPermission(DistanceRecord::class)
    private val caloriesPermission = HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
    private val heartRatePermission = HealthPermission.getReadPermission(HeartRateRecord::class)
    private val sleepPermission = HealthPermission.getReadPermission(SleepSessionRecord::class)

    override val requiredPermissions: Set<String> = setOf(
        stepsPermission,
        distancePermission,
        caloriesPermission,
        heartRatePermission,
        sleepPermission
    )

    override suspend fun getSdkStatus(): HealthConnectSdkStatus {
        return when (HealthConnectClient.getSdkStatus(appContext)) {
            HealthConnectClient.SDK_AVAILABLE -> HealthConnectSdkStatus.Available
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> HealthConnectSdkStatus.UpdateRequired
            else -> HealthConnectSdkStatus.NotInstalled
        }
    }

    override suspend fun getGrantedPermissions(): Set<String> {
        return try {
            if (getSdkStatus() != HealthConnectSdkStatus.Available) {
                emptySet()
            } else {
                client.permissionController.getGrantedPermissions()
            }
        } catch (e: Exception) {
            emptySet()
        }
    }

    override suspend fun readDaySummary(date: LocalDate, grantedPermissions: Set<String>): DailySummary {
        val hasSteps = stepsPermission in grantedPermissions
        val hasDist = distancePermission in grantedPermissions
        val hasCal = caloriesPermission in grantedPermissions
        val hasHr = heartRatePermission in grantedPermissions
        val hasSleep = sleepPermission in grantedPermissions

        val dayMetrics = buildSet {
            if (hasSteps) add(StepsRecord.COUNT_TOTAL)
            if (hasDist) add(DistanceRecord.DISTANCE_TOTAL)
            if (hasCal) add(TotalCaloriesBurnedRecord.ENERGY_TOTAL)
            if (hasHr) {
                add(HeartRateRecord.BPM_AVG)
                add(HeartRateRecord.BPM_MIN)
                add(HeartRateRecord.BPM_MAX)
            }
        }

        val dayRange = HealthDateRanges.dayRange(date)
        val dayResult = if (dayMetrics.isNotEmpty() && dayRange.isValid()) {
            try {
                client.aggregate(
                    AggregateRequest(
                        metrics = dayMetrics,
                        timeRangeFilter = TimeRangeFilter.between(dayRange.start, dayRange.end)
                    )
                )
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        val sleepRange = HealthDateRanges.sleepRange(date)
        val sleepResult = if (hasSleep && sleepRange.isValid()) {
            try {
                client.aggregate(
                    AggregateRequest(
                        metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(sleepRange.start, sleepRange.end)
                    )
                )
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        val steps = if (hasSteps) dayResult?.get(StepsRecord.COUNT_TOTAL) else null
        val distance = if (hasDist) dayResult?.get(DistanceRecord.DISTANCE_TOTAL)?.inMeters else null
        val calories = if (hasCal) dayResult?.get(TotalCaloriesBurnedRecord.ENERGY_TOTAL)?.inKilocalories else null
        var hrAvg = if (hasHr) dayResult?.get(HeartRateRecord.BPM_AVG) else null
        var hrMin = if (hasHr) dayResult?.get(HeartRateRecord.BPM_MIN) else null
        var hrMax = if (hasHr) dayResult?.get(HeartRateRecord.BPM_MAX) else null
        val sleepMin = if (hasSleep) sleepResult?.get(SleepSessionRecord.SLEEP_DURATION_TOTAL)?.toMinutes() else null

        // Fallback: si el aggregate de frecuencia cardíaca fue nulo, intentar leer muestras directas
        if (hasHr && hrAvg == null) {
            try {
                val hrResponse = client.readRecords(
                    ReadRecordsRequest(
                        recordType = HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(dayRange.start, dayRange.end),
                        pageSize = 200
                    )
                )
                val allBpm = hrResponse.records.flatMap { it.samples.map { s -> s.beatsPerMinute } }
                if (allBpm.isNotEmpty()) {
                    hrAvg = allBpm.average().toLong()
                    hrMin = allBpm.minOrNull()
                    hrMax = allBpm.maxOrNull()
                }
            } catch (e: Exception) {
                // Silencioso
            }
        }

        return DailySummary(
            date = date,
            steps = steps,
            distanceMeters = distance,
            caloriesKcal = calories,
            heartRateAvg = hrAvg,
            heartRateMin = hrMin,
            heartRateMax = hrMax,
            sleepMinutes = sleepMin,
            lastSync = Instant.now(),
            isFromCache = false
        )
    }

    override suspend fun readHeartRateSamples(date: LocalDate): List<HeartRateSample> {
        val granted = getGrantedPermissions()
        if (heartRatePermission !in granted) return emptyList()

        val range = HealthDateRanges.dayRange(date)
        if (!range.isValid()) return emptyList()

        val samples = mutableListOf<HeartRateSample>()
        var pageToken: String? = null

        try {
            do {
                val response = client.readRecords(
                    ReadRecordsRequest(
                        recordType = HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(range.start, range.end),
                        pageToken = pageToken
                    )
                )
                response.records.forEach { record ->
                    record.samples.forEach { sample ->
                        samples += HeartRateSample(time = sample.time, bpm = sample.beatsPerMinute)
                    }
                }
                pageToken = response.pageToken
            } while (pageToken != null)
        } catch (e: Exception) {
            // Devuelve lo que haya obtenido hasta el error
        }

        return samples.sortedBy { it.time }
    }

    override suspend fun readHistory(metric: MetricType, days: Int, grantedPermissions: Set<String>): HistoryData {
        val perm = when (metric) {
            MetricType.STEPS -> stepsPermission
            MetricType.DISTANCE -> distancePermission
            MetricType.CALORIES -> caloriesPermission
            MetricType.HEART_RATE_AVG -> heartRatePermission
            MetricType.SLEEP -> sleepPermission
        }

        val safeDays = days.coerceIn(1, 30)
        val today = LocalDate.now()
        val emptyPoints = (safeDays - 1 downTo 0).map { offset ->
            val date = today.minusDays(offset.toLong())
            HistoryPoint(date = date, value = 0.0, formattedValue = "0")
        }.toMutableList()

        if (perm !in grantedPermissions) {
            return HistoryData(
                metric = metric,
                days = safeDays,
                points = emptyPoints,
                hasPermission = false
            )
        }

        val startDate = today.minusDays((safeDays - 1).toLong())
        val startDateTime = LocalDateTime.of(startDate, java.time.LocalTime.MIN)
        val endDateTime = LocalDateTime.of(today, java.time.LocalTime.MAX)

        try {
            val aggregateMetric = when (metric) {
                MetricType.STEPS -> StepsRecord.COUNT_TOTAL
                MetricType.DISTANCE -> DistanceRecord.DISTANCE_TOTAL
                MetricType.CALORIES -> TotalCaloriesBurnedRecord.ENERGY_TOTAL
                MetricType.HEART_RATE_AVG -> HeartRateRecord.BPM_AVG
                MetricType.SLEEP -> SleepSessionRecord.SLEEP_DURATION_TOTAL
            }

            val response = client.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(aggregateMetric),
                    timeRangeFilter = TimeRangeFilter.between(startDateTime, endDateTime),
                    timeRangeSlicer = Period.ofDays(1)
                )
            )

            val valuesByDate = mutableMapOf<LocalDate, Double>()
            response.forEach { bucket ->
                val bucketDate = bucket.startTime.toLocalDate()
                val targetDate = if (metric == MetricType.SLEEP) bucketDate.plusDays(1) else bucketDate
                val numericValue: Double = when (metric) {
                    MetricType.STEPS -> bucket.result[StepsRecord.COUNT_TOTAL]?.toDouble() ?: 0.0
                    MetricType.DISTANCE -> (bucket.result[DistanceRecord.DISTANCE_TOTAL]?.inMeters ?: 0.0) / 1000.0
                    MetricType.CALORIES -> bucket.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories ?: 0.0
                    MetricType.HEART_RATE_AVG -> bucket.result[HeartRateRecord.BPM_AVG]?.toDouble() ?: 0.0
                    MetricType.SLEEP -> (bucket.result[SleepSessionRecord.SLEEP_DURATION_TOTAL]?.toMinutes()?.toDouble() ?: 0.0) / 60.0
                }
                valuesByDate[targetDate] = numericValue
            }

            val populatedPoints = emptyPoints.map { pt ->
                val value = valuesByDate[pt.date] ?: 0.0
                val formatted = when (metric) {
                    MetricType.STEPS -> String.format(Locale.getDefault(), "%,d", value.toLong())
                    MetricType.DISTANCE -> String.format(Locale.getDefault(), "%.2f km", value)
                    MetricType.CALORIES -> String.format(Locale.getDefault(), "%.0f kcal", value)
                    MetricType.HEART_RATE_AVG -> String.format(Locale.getDefault(), "%.0f lpm", value)
                    MetricType.SLEEP -> String.format(Locale.getDefault(), "%.1f h", value)
                }
                pt.copy(value = value, formattedValue = formatted)
            }

            return HistoryData(
                metric = metric,
                days = safeDays,
                points = populatedPoints,
                hasPermission = true
            )
        } catch (e: Exception) {
            return HistoryData(
                metric = metric,
                days = safeDays,
                points = emptyPoints,
                hasPermission = true
            )
        }
    }

    override suspend fun getDetectedSources(): List<HealthDeviceSource> {
        val detected = mutableSetOf<String>()
        try {
            // Revisar registros de pasos recientes para detectar el paquete emisor (ej. Samsung Health o Fitbit)
            val range = HealthDateRanges.dayRange(LocalDate.now())
            if (range.isValid() && stepsPermission in getGrantedPermissions()) {
                val response = client.readRecords(
                    ReadRecordsRequest(
                        recordType = StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(range.start, range.end),
                        pageSize = 10
                    )
                )
                response.records.forEach { record ->
                    record.metadata.dataOrigin.packageName.let { detected.add(it) }
                }
            }
        } catch (e: Exception) {
            // Ignorar y usar fallback
        }

        if (detected.isEmpty()) {
            // Fallback: verificar si Samsung Health está instalado en el teléfono
            try {
                val pm = appContext.packageManager
                pm.getPackageInfo("com.sec.android.app.shealth", 0)
                detected.add("com.sec.android.app.shealth")
            } catch (e: Exception) {
                // No está instalado Samsung Health
            }
        }

        return detected.map { pkg ->
            HealthDeviceResolver.resolveSource(pkg, appContext)
        }
    }
}
