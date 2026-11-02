package com.utn.health

import com.utn.health.data.healthconnect.HealthDateRanges
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HealthDateRangesTest {

    private val zoneId = ZoneId.of("UTC")
    private val testDate = LocalDate.of(2026, 11, 2)

    @Test
    fun testDayRange_coversFullDay() {
        val range = HealthDateRanges.dayRange(testDate, zoneId)

        assertTrue(range.isValid())
        assertEquals(Instant.parse("2026-11-02T00:00:00Z"), range.start)
        assertEquals(Instant.parse("2026-11-03T00:00:00Z"), range.end)
    }

    @Test
    fun testSleepRange_startsPreviousEvening() {
        val range = HealthDateRanges.sleepRange(testDate, zoneId)

        assertTrue(range.isValid())
        // Inicia a las 18:00 del día anterior (1 de Noviembre)
        assertEquals(Instant.parse("2026-11-01T18:00:00Z"), range.start)
        assertEquals(Instant.parse("2026-11-02T14:00:00Z"), range.end)
    }
}
