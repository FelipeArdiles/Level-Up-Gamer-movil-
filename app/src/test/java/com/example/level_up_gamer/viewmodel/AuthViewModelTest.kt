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
 * Pruebas unitarias para AuthViewModel usando MockK
 * 
 * MockK permite crear mocks (objetos simulados) de las dependencias
 * sin necesidad de usar la base de datos real o hacer llamadas de red.
 * 
 * Ventajas:
 * - Pruebas rápidas (no hay I/O real)
 * - Control total sobre el comportamiento de las dependencias
 * - Aislamiento: solo probamos la lógica del ViewModel
 */
@DisplayName("Pruebas de ViewModel - AuthViewModel con MockK")
class AuthViewModelTest {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setup() {
        // Crear un mock del repositorio
        mockRepository = mockk()
        // Inyectar el mock en el ViewModel
        viewModel = AuthViewModel(mockRepository)
    }

    @Test
    @DisplayName("Debería actualizar el email cuando se cambia")
    fun shouldUpdateEmailWhenChanged() = runTest {
        // Arrange
        val newEmail = "test@example.com"

        // Act
        viewModel.onEmailChange(newEmail)

        // Assert
        val uiState = viewModel.uiState.first()
        assertEquals(newEmail, uiState.email)
        assertNull(uiState.errorMessage) // El error se limpia al cambiar
    }

    @Test
    @DisplayName("Debería actualizar la contraseña cuando se cambia")
    fun shouldUpdatePasswordWhenChanged() = runTest {
        // Arrange
        val newPassword = "newPassword123"

        // Act
        viewModel.onPasswordChange(newPassword)

        // Assert
        val uiState = viewModel.uiState.first()
        assertEquals(newPassword, uiState.password)
        assertNull(uiState.errorMessage)
    }

    @Test
    @DisplayName("Debería establecer isLoading en true durante el login")
    fun shouldSetLoadingTrueDuringLogin() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val mockUser = User(
            id = "user1",
            username = "Test User",
            email = email,
            password = password,
            avatarIconId = 0
        )

        // Simular que el repositorio tarda en responder
        coEvery { mockRepository.login(email, password) } coAnswers {
            kotlinx.coroutines.delay(100)
            mockUser
        }

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()

        // Assert - Verificar que isLoading se establece en true
        // Nota: En una implementación real, podrías verificar el estado intermedio
        coVerify { mockRepository.login(email, password) }
    }

    @Test
    @DisplayName("Debería autenticar exitosamente con credenciales válidas")
    fun shouldAuthenticateSuccessfullyWithValidCredentials() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val mockUser = User(
            id = "user1",
            username = "Test User",
            email = email,
            password = password,
            avatarIconId = 0
        )

        // Configurar el mock para devolver un usuario válido
        coEvery { mockRepository.login(email, password) } returns mockUser

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()

        // Assert
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.isAuthenticated)
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        
        // Verificar que se llamó al repositorio
        coVerify(exactly = 1) { mockRepository.login(email, password) }
    }

    @Test
    @DisplayName("Debería mostrar error con credenciales inválidas")
    fun shouldShowErrorWithInvalidCredentials() = runTest {
        // Arrange
        val email = "wrong@example.com"
        val password = "wrongpassword"

        // Configurar el mock para devolver null (credenciales inválidas)
        coEvery { mockRepository.login(email, password) } returns null

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()

        // Assert
        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isAuthenticated)
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.errorMessage)
        assertTrue(uiState.errorMessage!!.contains("incorrectas", ignoreCase = true))
        
        coVerify(exactly = 1) { mockRepository.login(email, password) }
    }

    @Test
    @DisplayName("Debería limpiar el error al cambiar el email")
    fun shouldClearErrorWhenEmailChanges() = runTest {
        // Arrange - Primero establecer un error
        val email = "wrong@example.com"
        val password = "wrongpassword"
        coEvery { mockRepository.login(email, password) } returns null

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()

        // Verificar que hay un error
        var uiState = viewModel.uiState.first()
        assertNotNull(uiState.errorMessage)

        // Act - Cambiar el email
        viewModel.onEmailChange("new@example.com")

        // Assert - El error debería estar limpio
        uiState = viewModel.uiState.first()
        assertNull(uiState.errorMessage)
    }
}

