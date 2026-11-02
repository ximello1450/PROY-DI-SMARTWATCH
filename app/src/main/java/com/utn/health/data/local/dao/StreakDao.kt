package com.utn.health.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utn.health.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {
    @Query("SELECT * FROM racha_distancia WHERE id = 1 LIMIT 1")
    fun getStreak(): Flow<StreakEntity?>

    @Query("SELECT * FROM racha_distancia WHERE id = 1 LIMIT 1")
    suspend fun getStreakSync(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(streak: StreakEntity)
}
