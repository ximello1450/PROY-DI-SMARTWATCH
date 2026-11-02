package com.utn.health.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.utn.health.domain.model.ConnectionState
import com.utn.health.domain.usecase.CheckConnectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConnectionUiState(
    val connectionState: ConnectionState = ConnectionState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ConnectionViewModel(
    private val checkConnectionUseCase: CheckConnectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConnectionUiState(isLoading = true))
    val uiState: StateFlow<ConnectionUiState> = _uiState.asStateFlow()

    init {
        checkStatus()
    }

    fun checkStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                checkConnectionUseCase().collect { state ->
                    _uiState.update {
                        it.copy(
                            connectionState = state,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Error al verificar conexión"
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            checkConnectionUseCase.refresh()
            checkStatus()
        }
    }

    class Factory(
        private val checkConnectionUseCase: CheckConnectionUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ConnectionViewModel(checkConnectionUseCase) as T
        }
    }
}
