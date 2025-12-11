package com.example.level_up_gamer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.level_up_gamer.model.PurchaseItem

@Dao
interface PurchaseItemDao {
    @Insert
    suspend fun insertPurchaseItem(purchaseItem: PurchaseItem)

    @Insert
    suspend fun insertPurchaseItems(purchaseItems: List<PurchaseItem>)

    @Query("SELECT * FROM purchase_items WHERE purchaseId = :purchaseId")
    suspend fun getItemsByPurchaseId(purchaseId: Long): List<PurchaseItem>
}

