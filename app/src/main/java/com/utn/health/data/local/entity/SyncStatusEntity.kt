package com.utn.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "estado_sincronizacion")
data class SyncStatusEntity(
    @PrimaryKey
    val id: Int = 1,
    val permisosConcedidos: Boolean = false,
    val healthConnectDisponible: Boolean = false,
    val ultimaSincronizacion: Instant? = null,
    val fuentesDetectadas: String = ""
)
