package com.utn.health.domain.usecase

import com.utn.health.data.repository.HealthRepository
import com.utn.health.domain.model.StreakState
import kotlinx.coroutines.flow.Flow

class ManageStreakUseCase(
    private val repository: HealthRepository
) {
    operator fun invoke(): Flow<StreakState> {
        return repository.getStreakState()
    }

    suspend fun setDailyGoal(goalKm: Double) {
        if (goalKm > 0) {
            repository.setDailyDistanceGoal(goalKm)
        }
    }

    suspend fun activateFreeze(): Boolean {
        return repository.activateStreakFreeze()
    }

    suspend fun recoverStreak(): Boolean {
        return repository.recoverStreak()
    }

    suspend fun saveUserProfile(weightKg: Double, heightCm: Double) {
        if (weightKg > 0 && heightCm > 0) {
            repository.saveUserProfile(weightKg, heightCm)
        }
    }
}
