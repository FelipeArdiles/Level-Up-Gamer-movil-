package com.example.level_up_gamer.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    @DrawableRes val imageResId: Int, // 💡 Nuevo campo para el ID del recurso de imagen
    val stock: Int // 💡 Nuevo campo para el stock (simulado)
)