package com.utn.health.domain.provider

import com.utn.health.domain.model.DailySummary
import com.utn.health.domain.model.HealthConnectSdkStatus
import com.utn.health.domain.model.HealthDeviceSource
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.MetricType
import java.time.LocalDate

interface HealthConnectionProvider {
    val requiredPermissions: Set<String>
    suspend fun getSdkStatus(): HealthConnectSdkStatus
    suspend fun getGrantedPermissions(): Set<String>
    suspend fun readDaySummary(date: LocalDate, grantedPermissions: Set<String>): DailySummary
    suspend fun readHeartRateSamples(date: LocalDate): List<HeartRateSample>
    suspend fun readHistory(metric: MetricType, days: Int, grantedPermissions: Set<String>): HistoryData
    suspend fun getDetectedSources(): List<HealthDeviceSource>
}
