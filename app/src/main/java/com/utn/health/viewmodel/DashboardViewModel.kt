package com.utn.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.utn.health.domain.model.DailySummary
import com.utn.health.domain.model.StreakState
import com.utn.health.domain.usecase.GetTodaySummaryUseCase
import com.utn.health.domain.usecase.ManageStreakUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class DashboardUiState(
    val summary: DailySummary = DailySummary(date = LocalDate.now()),
    val streak: StreakState = StreakState(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class DashboardViewModel(
    private val getTodaySummaryUseCase: GetTodaySummaryUseCase,
    private val manageStreakUseCase: ManageStreakUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData(forceRefresh = false)
        observeStreak()
    }

    fun refresh() {
        loadData(forceRefresh = true)
    }

    private fun loadData(forceRefresh: Boolean) {
        viewModelScope.launch {
            _uiState.update { if (forceRefresh) it.copy(isRefreshing = true) else it.copy(isLoading = true) }
            try {
                getTodaySummaryUseCase(forceRefresh).collect { summary ->
                    _uiState.update {
                        it.copy(
                            summary = summary,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.localizedMessage ?: "Error al sincronizar datos de salud"
                    )
                }
            }
        }
    }

    private fun observeStreak() {
        viewModelScope.launch {
            manageStreakUseCase().collect { streak ->
                _uiState.update { it.copy(streak = streak) }
            }
        }
    }

    class Factory(
        private val getTodaySummaryUseCase: GetTodaySummaryUseCase,
        private val manageStreakUseCase: ManageStreakUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(getTodaySummaryUseCase, manageStreakUseCase) as T
        }
    }
}
