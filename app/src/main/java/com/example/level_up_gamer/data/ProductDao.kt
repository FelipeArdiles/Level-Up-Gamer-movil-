package com.example.level_up_gamer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.level_up_gamer.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id")
    suspend fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query("DELETE FROM products")
    suspend fun clear()

    @Query("UPDATE products SET stock = stock - :quantity WHERE id = :productId AND stock >= :quantity")
    suspend fun decreaseStock(productId: Int, quantity: Int): Int

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getById(productId: Int): Product?
}


