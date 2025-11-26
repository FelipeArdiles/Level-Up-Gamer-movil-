package com.example.level_up_gamer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.level_up_gamer.model.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAll(): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getByProductId(productId: Int): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)

    @Query("UPDATE cart_items SET quantity = quantity + :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: Int, quantity: Int)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clear()

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    suspend fun getTotalItems(): Int
}

