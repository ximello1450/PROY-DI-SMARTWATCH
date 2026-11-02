package com.utn.health.data.repository

import com.utn.health.domain.model.ConnectionState
import com.utn.health.domain.model.DailySummary
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.MetricType
import com.utn.health.domain.model.StreakState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HealthRepository {
    fun getTodaySummary(forceRefresh: Boolean = false): Flow<DailySummary>
    fun getHistory(metric: MetricType, days: Int): Flow<HistoryData>
    fun getHeartRateSamples(date: LocalDate): Flow<List<HeartRateSample>>
    fun getConnectionState(): Flow<ConnectionState>
    fun getStreakState(): Flow<StreakState>

    suspend fun setDailyDistanceGoal(goalKm: Double)
    suspend fun activateStreakFreeze(): Boolean
    suspend fun recoverStreak(): Boolean
    suspend fun saveUserProfile(weightKg: Double, heightCm: Double)
    suspend fun refreshAll()
}
