package com.utn.health.domain.usecase

import com.utn.health.data.repository.HealthRepository
import com.utn.health.domain.model.DailySummary
import kotlinx.coroutines.flow.Flow

class GetTodaySummaryUseCase(
    private val repository: HealthRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<DailySummary> {
        return repository.getTodaySummary(forceRefresh)
    }
}
