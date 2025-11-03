// viewmodel/UserViewModel.kt

package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.data.DatabaseProvider
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
        viewModelScope.launch {
            val user = DatabaseProvider.db().userDao().getAll().firstOrNull()
            _userProfile.value = user
        }
    }
}