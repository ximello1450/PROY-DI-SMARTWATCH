package com.utn.health.domain.usecase

import com.utn.health.data.repository.HealthRepository
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.MetricType
import kotlinx.coroutines.flow.Flow

class GetHistoryUseCase(
    private val repository: HealthRepository
) {
    operator fun invoke(metric: MetricType, days: Int): Flow<HistoryData> {
        return repository.getHistory(metric, days)
    }
}
