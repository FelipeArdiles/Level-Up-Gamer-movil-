// repository/AuthRepository.kt

package com.example.level_up_gamer.repository

import com.example.level_up_gamer.model.User
import kotlinx.coroutines.delay
import com.example.level_up_gamer.data.DatabaseProvider

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
        delay(500)
        val dao = DatabaseProvider.db().userDao()

        // 1. Buscar usuario por email y contraseña
        val user = dao.getByEmailAndPassword(email, password)
        if (user != null) {
            return user
        }

        // 2. Si no se encuentra, verificar si son las credenciales del usuario de demostración
        if (email == VALID_EMAIL && password == VALID_PASSWORD) {
            // Verificar si el usuario de demostración ya existe en la base de datos
            val demoUser = dao.getByEmail(VALID_EMAIL)
            if (demoUser == null) {
                // Si no existe, crearlo y persistirlo
                val newDemoUser = User(
                    id = "user123",
                    username = "Juan Gamer",
                    email = VALID_EMAIL,
                    password = VALID_PASSWORD,
                    avatarIconId = 0 // Icono por defecto
                )
                dao.insert(newDemoUser)
                return newDemoUser
            }
            // Si ya existe y la contraseña coincide (lo cual ya verificamos), devolverlo
            return demoUser
        }

        // 3. Si las credenciales no son válidas
        return null
    }

    suspend fun register(username: String, email: String, password: String): Result<User> {
        return try {
            val dao = DatabaseProvider.db().userDao()
            val existing = dao.getByEmail(email)
            if (existing != null) {
                return Result.failure(IllegalArgumentException("El email ya está registrado"))
            }
            val newUser = User(
                id = java.util.UUID.randomUUID().toString(),
                username = username,
                email = email,
                password = password,
                avatarIconId = 0 // Icono por defecto
            )
            dao.insert(newUser)
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}