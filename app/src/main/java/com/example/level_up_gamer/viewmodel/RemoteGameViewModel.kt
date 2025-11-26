package com.example.level_up_gamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer.model.RemoteGame
import com.example.level_up_gamer.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RemoteGameViewModel : ViewModel() {

    data class RemoteGameUiState(
        val isLoading: Boolean = false,
        val games: List<RemoteGame> = emptyList(),
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(RemoteGameUiState(isLoading = true))
    val uiState: StateFlow<RemoteGameUiState> = _uiState.asStateFlow()

    init {
        fetchRemoteGames()
    }

    fun refresh() = fetchRemoteGames()

    private fun fetchRemoteGames() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                val games = NetworkModule.gameApiService.getVideoGames(platform = "pc").take(12)
                _uiState.value = RemoteGameUiState(
                    isLoading = false,
                    games = games
                )
            } catch (e: Exception) {
                _uiState.value = RemoteGameUiState(
                    isLoading = false,
                    games = emptyList(),
                    errorMessage = "No se pudieron cargar las recomendaciones: ${e.message}"
                )
            }
        }
    }
}

