// viewmodel/UserViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.data.DatabaseProvider
import com.example.level_up_gamer.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    // Flow que observa el ID del usuario actual de la sesión
    private val currentUserIdFlow = MutableStateFlow<String?>(SessionManager.getCurrentUserId())

    // Flow del perfil del usuario en tiempo real
    val userProfile: StateFlow<User?> = currentUserIdFlow
        .flatMapLatest { userId ->
            if (userId != null) {
                DatabaseProvider.db().userDao().getByIdFlow(userId)
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    data class UserUiState(
        val isSaving: Boolean = false,
        val errorMessage: String? = null,
        val successMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        // Cargar el ID del usuario actual de la sesión
        refreshProfile()
    }

    fun refreshProfile() {
        // Actualizar el Flow con el ID del usuario actual de la sesión
        currentUserIdFlow.value = SessionManager.getCurrentUserId()
    }

    fun updateProfile(username: String, email: String, password: String?) {
        viewModelScope.launch {
            val currentUser = userProfile.value
            if (currentUser == null) {
                _uiState.value = UserUiState(errorMessage = "No se encontró un perfil para editar")
                return@launch
            }

            try {
                _uiState.value = UserUiState(isSaving = true)
                val newPassword = password?.takeIf { it.isNotBlank() } ?: currentUser.password
                val updatedUser = currentUser.copy(
                    username = username,
                    email = email,
                    password = newPassword
                )
                DatabaseProvider.db().userDao().insert(updatedUser)
                // El Flow se actualizará automáticamente cuando se actualice la base de datos
                _uiState.value = UserUiState(successMessage = "Perfil actualizado")
            } catch (e: Exception) {
                _uiState.value = UserUiState(
                    errorMessage = "No se pudo actualizar el perfil: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = UserUiState()
    }
}