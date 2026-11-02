package com.utn.health

import com.utn.health.domain.model.DailySummary
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class MetricConversionTest {

    @Test
    fun testDistanceFormatting_metersToKm() {
        val summary = DailySummary(
            date = LocalDate.now(),
            distanceMeters = 3450.0
        )
        assertEquals(3.45, summary.distanceKm ?: 0.0, 0.001)
        assertEquals("3.45 km", summary.distanceFormatted.replace(",", "."))
    }

    @Test
    fun testSleepFormatting_minutesToHoursAndMinutes() {
        val summary = DailySummary(
            date = LocalDate.now(),
            sleepMinutes = 445L // 7h 25m
        )
        assertEquals("7h 25m", summary.sleepFormatted)
    }

    @Test
    fun testCaloriesFormatting_roundedKcal() {
        val summary = DailySummary(
            date = LocalDate.now(),
            caloriesKcal = 560.8
        )
        assertEquals("561 kcal", summary.caloriesFormatted)
    }

    @Test
    fun testNullMetrics_produceCleanFallbacks() {
        val summary = DailySummary(date = LocalDate.now())
        assertEquals("--", summary.distanceFormatted)
        assertEquals("--", summary.sleepFormatted)
        assertEquals("--", summary.caloriesFormatted)
        assertEquals("--", summary.stepsFormatted)
        assertEquals("--", summary.heartRateAvgFormatted)
    }
}
