package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    data class RegistrationUiState(
        val username: String = "",
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val success: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun register() {
        val state = _uiState.value
        if (state.username.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Completa todos los campos")
            return
        }
        _uiState.value = state.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val result = repository.register(state.username, state.email, state.password)
            _uiState.value = result.fold(
                onSuccess = { state.copy(isLoading = false, success = true) },
                onFailure = { state.copy(isLoading = false, errorMessage = it.message ?: "Error al registrar") }
            )
        }
    }
}


