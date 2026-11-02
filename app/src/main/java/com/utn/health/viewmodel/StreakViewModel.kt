package com.utn.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.utn.health.domain.model.StreakState
import com.utn.health.domain.usecase.ManageStreakUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StreakUiState(
    val streak: StreakState = StreakState(),
    val actionMessage: String? = null,
    val isActionSuccess: Boolean = false
)

class StreakViewModel(
    private val manageStreakUseCase: ManageStreakUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StreakUiState())
    val uiState: StateFlow<StreakUiState> = _uiState.asStateFlow()

    init {
        observeStreak()
    }

    private fun observeStreak() {
        viewModelScope.launch {
            manageStreakUseCase().collect { state ->
                _uiState.update { it.copy(streak = state) }
            }
        }
    }

    fun updateGoal(newGoalKm: Double) {
        viewModelScope.launch {
            if (newGoalKm > 0) {
                manageStreakUseCase.setDailyGoal(newGoalKm)
                _uiState.update {
                    it.copy(
                        actionMessage = "Meta actualizada a ${newGoalKm} km",
                        isActionSuccess = true
                    )
                }
            }
        }
    }

    fun saveUserProfile(weightKg: Double, heightCm: Double) {
        viewModelScope.launch {
            if (weightKg in 25.0..250.0 && heightCm in 80.0..230.0) {
                manageStreakUseCase.saveUserProfile(weightKg, heightCm)
                _uiState.update {
                    it.copy(
                        actionMessage = "Perfil actualizado. Se calculó tu meta recomendada por IMC.",
                        isActionSuccess = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        actionMessage = "Por favor ingresa un peso y estatura válidos.",
                        isActionSuccess = false
                    )
                }
            }
        }
    }

    fun applyRecommendedGoal() {
        val rec = _uiState.value.streak.bmiRecommendation
        if (rec != null) {
            updateGoal(rec.recommendedDistanceKm)
        }
    }

    fun activateFreeze() {
        viewModelScope.launch {
            val success = manageStreakUseCase.activateFreeze()
            _uiState.update {
                it.copy(
                    actionMessage = if (success) "¡Escudo de racha activado para hoy!" else "No tienes escudos de congelamiento disponibles",
                    isActionSuccess = success
                )
            }
        }
    }

    fun recoverStreak() {
        viewModelScope.launch {
            val success = manageStreakUseCase.recoverStreak()
            _uiState.update {
                it.copy(
                    actionMessage = if (success) "¡Racha recuperada con éxito!" else "No fue posible recuperar la racha",
                    isActionSuccess = success
                )
            }
        }
    }

    fun clearActionMessage() {
        _uiState.update { it.copy(actionMessage = null) }
    }

    class Factory(
        private val manageStreakUseCase: ManageStreakUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StreakViewModel(manageStreakUseCase) as T
        }
    }
}
