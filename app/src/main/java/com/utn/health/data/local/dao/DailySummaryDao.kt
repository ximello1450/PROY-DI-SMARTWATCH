package com.utn.health.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utn.health.data.local.entity.DailySummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailySummaryDao {
    @Query("SELECT * FROM resumen_diario WHERE fecha = :date LIMIT 1")
    fun getSummaryByDate(date: String): Flow<DailySummaryEntity?>

    @Query("SELECT * FROM resumen_diario WHERE fecha = :date LIMIT 1")
    suspend fun getSummaryByDateSync(date: String): DailySummaryEntity?

    @Query("SELECT * FROM resumen_diario ORDER BY fecha DESC LIMIT :limit")
    fun getRecentSummaries(limit: Int): Flow<List<DailySummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(summary: DailySummaryEntity)
}
