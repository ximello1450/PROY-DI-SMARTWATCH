package com.utn.health.domain.usecase

import com.utn.health.data.repository.HealthRepository
import com.utn.health.domain.model.HeartRateSample
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetHeartRateDetailsUseCase(
    private val repository: HealthRepository
) {
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<List<HeartRateSample>> {
        return repository.getHeartRateSamples(date)
    }
}
