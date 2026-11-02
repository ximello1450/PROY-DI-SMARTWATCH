package com.utn.health.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utn.health.data.local.entity.SyncStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncStatusDao {
    @Query("SELECT * FROM estado_sincronizacion WHERE id = 1 LIMIT 1")
    fun getStatus(): Flow<SyncStatusEntity?>

    @Query("SELECT * FROM estado_sincronizacion WHERE id = 1 LIMIT 1")
    suspend fun getStatusSync(): SyncStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(status: SyncStatusEntity)
}
