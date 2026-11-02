package com.utn.health.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.utn.health.data.local.dao.DailySummaryDao
import com.utn.health.data.local.dao.StreakDao
import com.utn.health.data.local.dao.SyncStatusDao
import com.utn.health.data.local.entity.DailySummaryEntity
import com.utn.health.data.local.entity.StreakEntity
import com.utn.health.data.local.entity.SyncStatusEntity

@Database(
    entities = [
        DailySummaryEntity::class,
        StreakEntity::class,
        SyncStatusEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun dailySummaryDao(): DailySummaryDao
    abstract fun streakDao(): StreakDao
    abstract fun syncStatusDao(): SyncStatusDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getInstance(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "health_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
