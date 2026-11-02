package com.utn.health.data.healthconnect

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class DateRange(
    val start: Instant,
    val end: Instant
) {
    fun isValid(): Boolean = start.isBefore(end)
}

object HealthDateRanges {
    fun dayRange(
        date: LocalDate,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): DateRange {
        val startOfDay = date.atStartOfDay(zoneId).toInstant()
        val endOfDay = date.plusDays(1).atStartOfDay(zoneId).toInstant()
        return DateRange(start = startOfDay, end = endOfDay)
    }

    /**
     * El sueño de una noche inicia la tarde del día anterior (típicamente desde las 18:00)
     * y concluye hacia las 14:00 del día en que la persona despierta.
     */
    fun sleepRange(
        date: LocalDate,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): DateRange {
        val startOfSleepWindow = date.minusDays(1).atTime(18, 0).atZone(zoneId).toInstant()
        val endOfSleepWindow = date.atTime(14, 0).atZone(zoneId).toInstant()
        return DateRange(start = startOfSleepWindow, end = endOfSleepWindow)
    }
}
