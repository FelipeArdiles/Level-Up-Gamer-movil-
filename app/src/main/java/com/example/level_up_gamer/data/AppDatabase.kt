package com.example.level_up_gamer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.level_up_gamer.model.User

@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}


