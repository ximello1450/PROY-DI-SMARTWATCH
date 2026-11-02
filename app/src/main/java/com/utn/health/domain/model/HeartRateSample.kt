package com.utn.health.domain.model

import java.time.Instant
import java.time.LocalDate

data class HeartRateSample(
    val time: Instant,
    val bpm: Long
)

data class HistoryPoint(
    val date: LocalDate,
    val value: Double,
    val formattedValue: String
)

data class HistoryData(
    val metric: MetricType,
    val days: Int,
    val points: List<HistoryPoint>,
    val hasPermission: Boolean = true,
    val readAt: Instant = Instant.now()
)
