package com.utn.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.utn.health.domain.model.HistoryData
import com.utn.health.domain.model.MetricType
import com.utn.health.domain.usecase.GetHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryUiState(
    val selectedMetric: MetricType = MetricType.STEPS,
    val selectedDays: Int = 7,
    val historyData: HistoryData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HistoryViewModel(
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState(isLoading = true))
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun selectMetric(metric: MetricType) {
        if (_uiState.value.selectedMetric != metric) {
            _uiState.update { it.copy(selectedMetric = metric) }
            loadHistory()
        }
    }

    fun selectDays(days: Int) {
        if (_uiState.value.selectedDays != days) {
            _uiState.update { it.copy(selectedDays = days) }
            loadHistory()
        }
    }

    private fun loadHistory() {
        val currentMetric = _uiState.value.selectedMetric
        val currentDays = _uiState.value.selectedDays

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getHistoryUseCase(currentMetric, currentDays).collect { data ->
                    _uiState.update {
                        it.copy(
                            historyData = data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error al obtener historial"
                    )
                }
            }
        }
    }

    class Factory(
        private val getHistoryUseCase: GetHistoryUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoryViewModel(getHistoryUseCase) as T
        }
    }
}
