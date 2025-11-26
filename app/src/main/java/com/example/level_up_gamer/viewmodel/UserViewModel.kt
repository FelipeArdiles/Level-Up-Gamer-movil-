// viewmodel/UserViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.data.DatabaseProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile.asStateFlow()

    data class UserUiState(
        val isSaving: Boolean = false,
        val errorMessage: String? = null,
        val successMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun refreshProfile() {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val user = DatabaseProvider.db().userDao().getAll().firstOrNull()
            _userProfile.value = user
        }
    }

    fun updateProfile(username: String, email: String, password: String?) {
        val currentUser = _userProfile.value
        if (currentUser == null) {
            _uiState.value = UserUiState(errorMessage = "No se encontró un perfil para editar")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = UserUiState(isSaving = true)
                val newPassword = password?.takeIf { it.isNotBlank() } ?: currentUser.password
                val updatedUser = currentUser.copy(
                    username = username,
                    email = email,
                    password = newPassword
                )
                DatabaseProvider.db().userDao().insert(updatedUser)
                _userProfile.value = updatedUser
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