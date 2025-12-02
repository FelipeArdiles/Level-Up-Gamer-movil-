package com.example.level_up_gamer.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Assertions.*

/**
 * Pruebas del repositorio usando coroutines-test
 * 
 * Estas pruebas verifican el comportamiento asíncrono del repositorio
 * sin esperar delays reales.
 * 
 * Nota: Estas pruebas usan el repositorio real, que requiere una base de datos.
 * En un entorno de producción, sería mejor usar mocks, pero aquí demostramos
 * cómo coroutines-test puede controlar el tiempo virtual incluso con código real.
 */
@DisplayName("Pruebas de Repositorio - Operaciones Asíncronas con coroutines-test")
class AuthRepositoryCoroutinesTest {

    private val repository = AuthRepository()
    private val testDispatcher = StandardTestDispatcher()

    @Test
    @DisplayName("Debería completar el login de forma asíncrona con control de tiempo virtual")
    fun shouldCompleteLoginAsynchronously() = runTest(testDispatcher) {
        // Arrange
        val email = "test@gamer.com"
        val password = "password123"

        // Act - El repositorio tiene un delay de 500ms
        // Con coroutines-test, podemos avanzar el tiempo virtual
        val loginJob = kotlinx.coroutines.launch {
            repository.login(email, password)
        }

        // Avanzar el tiempo virtual para completar el delay
        // Esto ejecuta las coroutines sin esperar realmente 500ms
        advanceTimeBy(500)

        // Assert - La operación debería completarse
        // Nota: En un entorno real, verificarías el resultado
        // pero aquí solo demostramos el control del tiempo virtual
        loginJob.join()
    }

    @Test
    @DisplayName("Debería demostrar el control de tiempo virtual en operaciones asíncronas")
    fun shouldDemonstrateVirtualTimeControl() = runTest(testDispatcher) {
        // Este test demuestra cómo coroutines-test controla el tiempo virtual
        
        var executed = false
        
        val asyncOperation = kotlinx.coroutines.launch {
            delay(1000) // Simula una operación que tarda 1 segundo
            executed = true
        }

        // Avanzar el tiempo virtual en 500ms
        advanceTimeBy(500)
        assertFalse(executed) // Aún no debería ejecutarse

        // Avanzar otros 500ms
        advanceTimeBy(500)
        asyncOperation.join()
        assertTrue(executed) // Ahora debería estar ejecutado
    }
}

