package com.example.level_up_gamer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "purchases",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val totalAmount: Double,
    val purchaseDate: Long = System.currentTimeMillis() // Timestamp en milisegundos
)

