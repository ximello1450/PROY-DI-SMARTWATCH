package com.utn.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "resumen_diario")
data class DailySummaryEntity(
    @PrimaryKey
    val fecha: String,
    val pasos: Long? = null,
    val distanciaMetros: Double? = null,
    val caloriasKcal: Double? = null,
    val fcPromedio: Long? = null,
    val fcMin: Long? = null,
    val fcMax: Long? = null,
    val minutosSueno: Long? = null,
    val ultimaSincronizacion: Instant? = null
)
