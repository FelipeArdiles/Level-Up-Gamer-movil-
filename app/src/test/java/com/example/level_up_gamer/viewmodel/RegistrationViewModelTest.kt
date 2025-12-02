package com.example.level_up_gamer.viewmodel

import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * Pruebas unitarias para RegistrationViewModel usando MockK
 * 
 * Estas pruebas verifican la lógica de registro sin dependencias reales.
 */
@DisplayName("Pruebas de ViewModel - RegistrationViewModel con MockK")
class RegistrationViewModelTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: RegistrationViewModel

    @BeforeEach
    fun setup() {
        mockRepository = mockk()
        viewModel = RegistrationViewModel(mockRepository)
    }

    @Test
    @DisplayName("Debería actualizar el username cuando se cambia")
    fun shouldUpdateUsernameWhenChanged() = runTest {
        val newUsername = "NuevoUsuario"
        viewModel.onUsernameChange(newUsername)
        
        val uiState = viewModel.uiState.first()
        assertEquals(newUsername, uiState.username)
        assertNull(uiState.errorMessage)
    }

    @Test
    @DisplayName("Debería registrar exitosamente con datos válidos")
    fun shouldRegisterSuccessfullyWithValidData() = runTest {
        // Arrange
        val username = "TestUser"
        val email = "test@example.com"
        val password = "password123"
        val mockUser = User(
            id = "user123",
            username = username,
            email = email,
            password = password,
            avatarIconId = 0
        )

        coEvery { mockRepository.register(username, email, password) } returns 
            Result.success(mockUser)

        viewModel.onUsernameChange(username)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.register()

        // Assert
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.success)
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        
        coVerify(exactly = 1) { mockRepository.register(username, email, password) }
    }

    @Test
    @DisplayName("Debería mostrar error cuando faltan campos")
    fun shouldShowErrorWhenFieldsAreEmpty() = runTest {
        // Arrange - Campos vacíos
        viewModel.onUsernameChange("")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")

        // Act
        viewModel.register()

        // Assert
        val uiState = viewModel.uiState.first()
        assertFalse(uiState.success)
        assertNotNull(uiState.errorMessage)
        assertTrue(uiState.errorMessage!!.contains("Completa todos los campos", ignoreCase = true))
        
        // No debería llamar al repositorio si faltan campos
        coVerify(exactly = 0) { mockRepository.register(any(), any(), any()) }
    }

    @Test
    @DisplayName("Debería mostrar error cuando el email ya está registrado")
    fun shouldShowErrorWhenEmailAlreadyExists() = runTest {
        // Arrange
        val username = "TestUser"
        val email = "existing@example.com"
        val password = "password123"

        coEvery { mockRepository.register(username, email, password) } returns 
            Result.failure(IllegalArgumentException("El email ya está registrado"))

        viewModel.onUsernameChange(username)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.register()

        // Assert
        val uiState = viewModel.uiState.first()
        assertFalse(uiState.success)
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.errorMessage)
        assertTrue(uiState.errorMessage!!.contains("registrado", ignoreCase = true))
    }

    @Test
    @DisplayName("Debería limpiar el error al cambiar cualquier campo")
    fun shouldClearErrorWhenAnyFieldChanges() = runTest {
        // Arrange - Establecer un error primero
        viewModel.onUsernameChange("")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.register()

        var uiState = viewModel.uiState.first()
        assertNotNull(uiState.errorMessage)

        // Act - Cambiar un campo
        viewModel.onUsernameChange("NuevoUsuario")

        // Assert
        uiState = viewModel.uiState.first()
        assertNull(uiState.errorMessage)
    }
}

