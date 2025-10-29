// viewmodel/AuthViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // 1. Estado de la pantalla (UI State)
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // Clase de datos que representa todos los estados posibles de la UI de Login
    data class LoginUiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isAuthenticated: Boolean = false
    )

    // Evento de la UI: El usuario ingresa un email
    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            errorMessage = null // Limpia el error al cambiar el texto
        )
    }

    // Evento de la UI: El usuario ingresa una contraseña
    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            errorMessage = null // Limpia el error al cambiar el texto
        )
    }

    // Evento de la UI: El usuario presiona el botón de Login
    fun login() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val user = repository.login(
                _uiState.value.email,
                _uiState.value.password
            )

            _uiState.value = if (user != null) {
                // Éxito
                _uiState.value.copy(isAuthenticated = true, isLoading = false)
            } else {
                // Fallo
                _uiState.value.copy(
                    errorMessage = "Credenciales incorrectas. Prueba con test@gamer.com / password123",
                    isLoading = false
                )
            }
        }
    }
}