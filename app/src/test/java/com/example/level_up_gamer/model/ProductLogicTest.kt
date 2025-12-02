package com.example.level_up_gamer.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/**
 * Pruebas de lógica de negocio para el modelo Product usando Kotest
 * 
 * Usa FunSpec, otro estilo de Kotest más simple y directo.
 */
class ProductLogicTest : FunSpec({

    test("Product debería tener un precio positivo") {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 10.0,
            description = "Test",
            imageResId = 0,
            stock = 5
        )

        product.price shouldBe 10.0
        product.price shouldNotBe 0.0
    }

    test("Product debería tener stock no negativo") {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 10.0,
            description = "Test",
            imageResId = 0,
            stock = 5
        )

        product.stock shouldBe 5
        product.stock shouldNotBe -1
    }

    test("Product debería calcular el precio total correctamente") {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 15.5,
            description = "Test",
            imageResId = 0,
            stock = 10
        )

        val totalPrice = product.price * 3
        totalPrice shouldBe 46.5
    }

    test("Product debería verificar disponibilidad de stock") {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 10.0,
            description = "Test",
            imageResId = 0,
            stock = 5
        )

        (product.stock >= 3) shouldBe true
        (product.stock >= 6) shouldBe false
    }

    test("Product con stock cero debería estar agotado") {
        val product = Product(
            id = 1,
            name = "Test Product",
            price = 10.0,
            description = "Test",
            imageResId = 0,
            stock = 0
        )

        (product.stock > 0) shouldBe false
    }
})

