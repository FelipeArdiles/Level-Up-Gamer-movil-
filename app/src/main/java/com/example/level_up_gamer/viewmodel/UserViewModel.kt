// viewmodel/UserViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    // El estado (StateFlow) que la vista observará. Puede ser nulo si no hay usuario cargado.
    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> = _userProfile

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        // En una app real, llamarías a un Repositorio aquí para obtener los datos del servidor.
        viewModelScope.launch {
            // Simulación de carga de datos del usuario logueado
            val simulatedUser = User(
                id = "user123",
                username = "Juan Gamer",
                email = "test@gamer.com"
            )
            _userProfile.value = simulatedUser
        }
    }
}