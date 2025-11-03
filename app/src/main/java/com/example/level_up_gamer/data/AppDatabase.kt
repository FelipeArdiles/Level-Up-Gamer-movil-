package com.example.level_up_gamer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.level_up_gamer.model.User
import com.example.level_up_gamer.model.Product
import com.example.level_up_gamer.model.CartItem

@Database(entities = [User::class, Product::class, CartItem::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}


