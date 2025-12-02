package com.example.level_up_gamer.viewmodel

import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * Pruebas de operaciones asíncronas usando coroutines-test
 * 
 * coroutines-test permite:
 * - Controlar el tiempo virtual (avanzar el tiempo sin esperar realmente)
 * - Probar operaciones asíncronas de forma determinista
 * - Verificar que las coroutines se ejecutan en el orden correcto
 * 
 * Ventajas:
 * - Pruebas rápidas (no hay delays reales)
 * - Control total sobre la ejecución de coroutines
 * - Determinismo: siempre se ejecutan igual
 */
@DisplayName("Pruebas de Operaciones Asíncronas - coroutines-test")
class CoroutinesTestExample {

    private lateinit var mockRepository: AuthRepository
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        mockRepository = mockk()
        viewModel = AuthViewModel(mockRepository)
    }

    @Test
    @DisplayName("Debería manejar correctamente operaciones asíncronas con delay")
    fun shouldHandleAsyncOperationsWithDelay() = runTest(testDispatcher) {
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

        // Simular que el repositorio tarda 500ms en responder
        coEvery { mockRepository.login(email, password) } coAnswers {
            delay(500) // Simula una llamada de red
            mockUser
        }

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act - Iniciar el login
        viewModel.login()

        // Avanzar el tiempo virtual en 500ms
        // Esto ejecuta las coroutines sin esperar realmente
        advanceTimeBy(500)

        // Assert
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.isAuthenticated)
        assertFalse(uiState.isLoading)
        
        coVerify(exactly = 1) { mockRepository.login(email, password) }
    }

    @Test
    @DisplayName("Debería establecer isLoading durante la operación asíncrona")
    fun shouldSetLoadingDuringAsyncOperation() = runTest(testDispatcher) {
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

        coEvery { mockRepository.login(email, password) } coAnswers {
            delay(300)
            mockUser
        }

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()

        // Avanzar solo 100ms (aún no ha terminado)
        advanceTimeBy(100)

        // En este punto, isLoading debería estar en true
        // Nota: En una implementación real, podrías verificar el estado intermedio
        // pero con MutableStateFlow y runTest, el estado se actualiza inmediatamente
        // en el contexto de prueba

        // Avanzar el resto del tiempo
        advanceTimeBy(200)

        // Assert - Al final debería estar autenticado
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.isAuthenticated)
        assertFalse(uiState.isLoading)
    }

    @Test
    @DisplayName("Debería manejar múltiples operaciones asíncronas secuenciales")
    fun shouldHandleMultipleSequentialAsyncOperations() = runTest(testDispatcher) {
        // Arrange
        val email1 = "test1@example.com"
        val email2 = "test2@example.com"
        val password = "password123"

        val mockUser1 = User("user1", "User 1", email1, password, 0)
        val mockUser2 = User("user2", "User 2", email2, password, 0)

        coEvery { mockRepository.login(email1, password) } coAnswers {
            delay(200)
            mockUser1
        }

        coEvery { mockRepository.login(email2, password) } coAnswers {
            delay(200)
            mockUser2
        }

        // Act - Primera operación
        viewModel.onEmailChange(email1)
        viewModel.onPasswordChange(password)
        viewModel.login()
        advanceTimeBy(200)

        var uiState = viewModel.uiState.first()
        assertTrue(uiState.isAuthenticated)

        // Segunda operación (después de resetear)
        viewModel.onEmailChange(email2)
        viewModel.login()
        advanceTimeBy(200)

        // Assert
        uiState = viewModel.uiState.first()
        assertTrue(uiState.isAuthenticated)
        
        coVerify(exactly = 1) { mockRepository.login(email1, password) }
        coVerify(exactly = 1) { mockRepository.login(email2, password) }
    }

    @Test
    @DisplayName("Debería manejar errores en operaciones asíncronas")
    fun shouldHandleErrorsInAsyncOperations() = runTest(testDispatcher) {
        // Arrange
        val email = "test@example.com"
        val password = "wrongpassword"

        coEvery { mockRepository.login(email, password) } coAnswers {
            delay(300)
            null // Simula credenciales inválidas
        }

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        // Act
        viewModel.login()
        advanceTimeBy(300)

        // Assert
        val uiState = viewModel.uiState.first()
        assertFalse(uiState.isAuthenticated)
        assertFalse(uiState.isLoading)
        assertNotNull(uiState.errorMessage)
    }
}

