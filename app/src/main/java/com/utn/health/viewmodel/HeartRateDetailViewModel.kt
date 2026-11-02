package com.utn.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.utn.health.domain.model.HeartRateSample
import com.utn.health.domain.usecase.GetHeartRateDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HeartRateDetailUiState(
    val samples: List<HeartRateSample> = emptyList(),
    val averageBpm: Long? = null,
    val minBpm: Long? = null,
    val maxBpm: Long? = null,
    val latestBpm: Long? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HeartRateDetailViewModel(
    private val getHeartRateDetailsUseCase: GetHeartRateDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeartRateDetailUiState(isLoading = true))
    val uiState: StateFlow<HeartRateDetailUiState> = _uiState.asStateFlow()

    init {
        loadHeartRateSamples()
    }

    fun loadHeartRateSamples(date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getHeartRateDetailsUseCase(date).collect { samples ->
                    val avg = if (samples.isNotEmpty()) samples.map { it.bpm }.average().toLong() else null
                    val min = samples.minOfOrNull { it.bpm }
                    val max = samples.maxOfOrNull { it.bpm }
                    val latest = samples.lastOrNull()?.bpm

                    _uiState.update {
                        it.copy(
                            samples = samples,
                            averageBpm = avg,
                            minBpm = min,
                            maxBpm = max,
                            latestBpm = latest,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error al obtener frecuencia cardíaca"
                    )
                }
            }
        }
    }

    class Factory(
        private val getHeartRateDetailsUseCase: GetHeartRateDetailsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HeartRateDetailViewModel(getHeartRateDetailsUseCase) as T
        }
    }
}
