package com.utn.health.domain.model

import java.time.Instant
import java.time.LocalDate
import java.util.Locale

data class DailySummary(
    val date: LocalDate,
    val steps: Long? = null,
    val distanceMeters: Double? = null,
    val caloriesKcal: Double? = null,
    val heartRateAvg: Long? = null,
    val heartRateMin: Long? = null,
    val heartRateMax: Long? = null,
    val sleepMinutes: Long? = null,
    val lastSync: Instant? = null,
    val isFromCache: Boolean = false
) {
    val distanceKm: Double?
        get() = distanceMeters?.let { it / 1000.0 }

    val distanceFormatted: String
        get() = distanceKm?.let { String.format(Locale.getDefault(), "%.2f km", it) } ?: "--"

    val caloriesFormatted: String
        get() = caloriesKcal?.let { String.format(Locale.getDefault(), "%.0f kcal", it) } ?: "--"

    val stepsFormatted: String
        get() = steps?.let { String.format(Locale.getDefault(), "%,d", it) } ?: "--"

    val heartRateAvgFormatted: String
        get() = heartRateAvg?.let { "$it lpm" } ?: "--"

    val sleepFormatted: String
        get() {
            val total = sleepMinutes ?: return "--"
            val hours = total / 60
            val minutes = total % 60
            return "${hours}h ${minutes}m"
        }
}
