// repository/AuthRepository.kt

package com.example.level_up_gamer.repository

import com.example.level_up_gamer.model.User
import kotlinx.coroutines.delay

/**
 * Repositorio de Autenticación.
 * En una aplicación real, esta clase manejaría llamadas a Firebase, API REST, o base de datos.
 */
class AuthRepository {

    // Credenciales de prueba (hardcoded)
    private val VALID_EMAIL = "test@gamer.com"
    private val VALID_PASSWORD = "password123"

    /**
     * Simula el proceso de inicio de sesión con una demora de red.
     * @return User si las credenciales son válidas, o null si fallan.
     */
    suspend fun login(email: String, password: String): User? {
        // 1. Simular latencia de red (2 segundos)
        delay(2000)

        // 2. Simular validación de credenciales
        return if (email == VALID_EMAIL && password == VALID_PASSWORD) {
            User(
                id = "user123",
                username = "Juan Gamer",
                email = VALID_EMAIL
            )
        } else {
            null // Autenticación fallida
        }
    }
}