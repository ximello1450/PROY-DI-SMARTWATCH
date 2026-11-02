package com.utn.health

import com.utn.health.domain.model.StreakState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StreakEngineTest {

    @Test
    fun testStreakState_progressPercentage() {
        val state = StreakState(
            dailyDistanceGoalKm = 5.0,
            todayDistanceKm = 2.5
        )
        assertEquals(0.5f, state.progressPercentage, 0.001f)
        assertEquals(2.5, state.remainingDistanceKm, 0.001)
    }

    @Test
    fun testStreakState_goalCompleted() {
        val state = StreakState(
            dailyDistanceGoalKm = 3.0,
            todayDistanceKm = 3.5,
            isGoalCompletedToday = true
        )
        assertEquals(1.0f, state.progressPercentage, 0.001f)
        assertEquals(0.0, state.remainingDistanceKm, 0.001)
        assertTrue(state.isGoalCompletedToday)
    }

    @Test
    fun testStreakState_streakAtRiskWhenGoalNotMet() {
        val state = StreakState(
            currentStreak = 4,
            dailyDistanceGoalKm = 4.0,
            todayDistanceKm = 1.0,
            isFrozenToday = false,
            isStreakAtRisk = true
        )
        assertTrue(state.isStreakAtRisk)
        assertFalse(state.isFrozenToday)
    }

    @Test
    fun testStreakState_frozenProtectsStreak() {
        val state = StreakState(
            currentStreak = 10,
            dailyDistanceGoalKm = 5.0,
            todayDistanceKm = 0.5,
            isFrozenToday = true,
            isStreakAtRisk = false
        )
        assertTrue(state.isFrozenToday)
        assertFalse(state.isStreakAtRisk)
    }
}
