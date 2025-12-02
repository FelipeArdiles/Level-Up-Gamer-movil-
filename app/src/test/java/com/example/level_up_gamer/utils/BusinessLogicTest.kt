package com.example.level_up_gamer.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.email
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Pruebas de lógica de negocio usando Kotest
 * 
 * Kotest es un framework de pruebas moderno para Kotlin que ofrece:
 * - Múltiples estilos de pruebas (DescribeSpec, FunSpec, etc.)
 * - Property-based testing (pruebas con datos generados automáticamente)
 * - Aserciones más legibles y expresivas
 * - Mejor integración con Kotlin
 * 
 * Ventajas:
 * - Código más legible y expresivo
 * - Property testing para encontrar casos edge
 * - Mejor soporte para Kotlin
 */
class BusinessLogicTest : DescribeSpec({

    describe("Validación de Email") {
        it("debería validar emails correctos") {
            val validEmails = listOf(
                "test@example.com",
                "user.name@domain.co.uk",
                "user+tag@example.com"
            )

            validEmails.forEach { email ->
                isValidEmail(email) shouldBe true
            }
        }

        it("debería rechazar emails inválidos") {
            val invalidEmails = listOf(
                "notanemail",
                "@example.com",
                "test@",
                "test..test@example.com",
                ""
            )

            invalidEmails.forEach { email ->
                isValidEmail(email) shouldBe false
            }
        }

        // Property-based testing: genera emails aleatorios y los prueba
        it("debería validar emails generados aleatoriamente") {
            checkAll(Arb.email()) { email ->
                isValidEmail(email) shouldBe true
            }
        }
    }

    describe("Validación de Contraseña") {
        it("debería aceptar contraseñas con longitud mínima") {
            val validPasswords = listOf(
                "password123",
                "SecurePass!",
                "12345678"
            )

            validPasswords.forEach { password ->
                isValidPassword(password) shouldBe true
            }
        }

        it("debería rechazar contraseñas muy cortas") {
            val shortPasswords = listOf(
                "123",
                "abc",
                ""
            )

            shortPasswords.forEach { password ->
                isValidPassword(password) shouldBe false
            }
        }

        // Property-based testing para contraseñas
        it("debería validar contraseñas generadas aleatoriamente") {
            checkAll(Arb.string(minSize = 8, maxSize = 50)) { password ->
                if (password.length >= 8) {
                    isValidPassword(password) shouldBe true
                }
            }
        }
    }

    describe("Validación de Nombre de Usuario") {
        it("debería aceptar nombres de usuario válidos") {
            val validUsernames = listOf(
                "Usuario123",
                "user_name",
                "User-Name",
                "TestUser"
            )

            validUsernames.forEach { username ->
                isValidUsername(username) shouldBe true
            }
        }

        it("debería rechazar nombres de usuario inválidos") {
            val invalidUsernames = listOf(
                "",
                " ",
                "a", // Muy corto
                "user@name" // Caracteres especiales no permitidos
            )

            invalidUsernames.forEach { username ->
                isValidUsername(username) shouldBe false
            }
        }
    }

    describe("Cálculo de Precio Total") {
        it("debería calcular el precio total correctamente") {
            calculateTotalPrice(10.0, 3) shouldBe 30.0
            calculateTotalPrice(5.5, 2) shouldBe 11.0
            calculateTotalPrice(0.0, 5) shouldBe 0.0
        }

        it("debería manejar cantidades cero") {
            calculateTotalPrice(10.0, 0) shouldBe 0.0
        }

        it("debería manejar precios negativos como cero") {
            calculateTotalPrice(-5.0, 2) shouldBe 0.0
        }
    }

    describe("Validación de Stock") {
        it("debería verificar stock disponible") {
            hasStockAvailable(10, 5) shouldBe true
            hasStockAvailable(10, 10) shouldBe true
            hasStockAvailable(10, 11) shouldBe false
            hasStockAvailable(0, 1) shouldBe false
        }
    }
})

// Funciones auxiliares de lógica de negocio para probar

/**
 * Valida si un email tiene formato correcto
 */
fun isValidEmail(email: String): Boolean {
    if (email.isBlank()) return false
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
    return emailRegex.matches(email)
}

/**
 * Valida si una contraseña cumple con los requisitos mínimos
 */
fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}

/**
 * Valida si un nombre de usuario es válido
 */
fun isValidUsername(username: String): Boolean {
    if (username.isBlank() || username.length < 3) return false
    val usernameRegex = "^[A-Za-z0-9_-]+\$".toRegex()
    return usernameRegex.matches(username)
}

/**
 * Calcula el precio total de un producto
 */
fun calculateTotalPrice(unitPrice: Double, quantity: Int): Double {
    if (unitPrice < 0 || quantity < 0) return 0.0
    return unitPrice * quantity
}

/**
 * Verifica si hay stock disponible
 */
fun hasStockAvailable(availableStock: Int, requestedQuantity: Int): Boolean {
    return availableStock >= requestedQuantity && availableStock > 0
}

