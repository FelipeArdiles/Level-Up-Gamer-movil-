package com.example.level_up_gamer.data

import android.content.Context
import androidx.room.Room
import java.util.concurrent.Executors
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Product

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
                    // Semilla de productos por defecto si la tabla está vacía
                    Executors.newSingleThreadExecutor().execute {
                        val dao = instance!!.productDao()
                        val count = dao.count()
                        if (count == 0) {
                            dao.insertAll(
                                listOf(
                                    Product(
                                        id = 1,
                                        name = "Elden Ring: Shadow of the Erdtree Edition",
                                        price = 89.99,
                                        description = "Aventura épica de mundo abierto de FromSoftware. Edición con DLC.",
                                        imageResId = R.drawable.elden_ring,
                                        stock = 5
                                    ),
                                    Product(
                                        id = 2,
                                        name = "The Legend of Zelda: Tears of the Kingdom",
                                        price = 69.99,
                                        description = "La última entrega de la saga de Hyrule, exploración y construcción.",
                                        imageResId = R.drawable.zelda_totk,
                                        stock = 0
                                    ),
                                    Product(
                                        id = 3,
                                        name = "Cyberpunk 2077 (Ultimate Edition)",
                                        price = 59.99,
                                        description = "RPG futurista en Night City con la expansión Phantom Liberty.",
                                        imageResId = R.drawable.cyberpunk,
                                        stock = 12
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun db(): AppDatabase {
        return requireNotNull(instance) { "DatabaseProvider no inicializado. Llama init(context) primero." }
    }
}


