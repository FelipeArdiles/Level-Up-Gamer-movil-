package com.example.level_up_gamer.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    @DrawableRes val imageResId: Int, // 💡 ID del recurso de imagen (para imágenes predefinidas)
    val imagePath: String? = null, // 💡 Ruta del archivo de imagen (para imágenes cargadas desde dispositivo)
    val stock: Int // 💡 Stock disponible
)