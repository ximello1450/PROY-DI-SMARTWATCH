package com.utn.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racha_distancia")
data class StreakEntity(
    @PrimaryKey
    val id: Int = 1,
    val rachaActual: Int = 0,
    val mejorRacha: Int = 0,
    val metaKm: Double = 3.0,
    val congeladoresDisponibles: Int = 2,
    val estaCongeladaHoy: Boolean = false,
    val rachaRecuperable: Boolean = false,
    val conteoRecuperable: Int = 0,
    val ultimaFechaCompletada: String? = null,
    val fechasCompletadasCsv: String = "",
    val pesoKg: Double? = null,
    val estaturaCm: Double? = null
)
