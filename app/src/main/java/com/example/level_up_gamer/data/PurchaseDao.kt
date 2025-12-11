package com.example.level_up_gamer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.level_up_gamer.model.Purchase
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertPurchase(purchase: Purchase): Long

    @Query("SELECT * FROM purchases WHERE userId = :userId ORDER BY purchaseDate DESC")
    suspend fun getPurchasesByUserId(userId: String): List<Purchase>

    @Query("SELECT * FROM purchases WHERE userId = :userId ORDER BY purchaseDate DESC")
    fun getPurchasesByUserIdFlow(userId: String): Flow<List<Purchase>>

    @Query("SELECT COALESCE(SUM(totalAmount), 0) FROM purchases WHERE userId = :userId")
    suspend fun getTotalSpentByUserId(userId: String): Double

    @Query("SELECT COUNT(*) FROM purchases WHERE userId = :userId")
    suspend fun getPurchaseCountByUserId(userId: String): Int
}

