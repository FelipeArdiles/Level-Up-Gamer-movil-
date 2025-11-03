package com.example.level_up_gamer.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    fun init(context: Context) {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "levelupgamer.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
    }

    fun db(): AppDatabase {
        return requireNotNull(instance) { "DatabaseProvider no inicializado. Llama init(context) primero." }
    }
}


