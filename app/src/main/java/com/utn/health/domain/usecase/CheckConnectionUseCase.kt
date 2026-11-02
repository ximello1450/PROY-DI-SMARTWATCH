package com.utn.health.domain.usecase

import com.utn.health.data.repository.HealthRepository
import com.utn.health.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

class CheckConnectionUseCase(
    private val repository: HealthRepository
) {
    operator fun invoke(): Flow<ConnectionState> {
        return repository.getConnectionState()
    }

    suspend fun refresh() {
        repository.refreshAll()
    }
}
