package com.utn.health.domain.model

import java.time.LocalDate

data class StreakState(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val dailyDistanceGoalKm: Double = 3.0,
    val todayDistanceKm: Double = 0.0,
    val isGoalCompletedToday: Boolean = false,
    val streakFreezesAvailable: Int = 2,
    val isFrozenToday: Boolean = false,
    val isStreakAtRisk: Boolean = false,
    val isStreakRecoverable: Boolean = false,
    val recoverableStreakCount: Int = 0,
    val lastCompletedDate: LocalDate? = null,
    val completedDates: Set<LocalDate> = emptySet(),
    val userWeightKg: Double? = null,
    val userHeightCm: Double? = null
) {
    val bmiRecommendation: BmiRecommendation?
        get() = BmiRecommendation.calculate(userWeightKg, userHeightCm)

    val progressPercentage: Float
        get() = if (dailyDistanceGoalKm > 0) {
            ((todayDistanceKm / dailyDistanceGoalKm).coerceIn(0.0, 1.0)).toFloat()
        } else 1.0f

    val remainingDistanceKm: Double
        get() = (dailyDistanceGoalKm - todayDistanceKm).coerceAtLeast(0.0)
}
