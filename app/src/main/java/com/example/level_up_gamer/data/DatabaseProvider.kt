package com.example.level_up_gamer.data

import android.content.Context
import androidx.room.Room
import com.example.level_up_gamer.R
import com.example.level_up_gamer.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
                    // Semilla de productos por defecto si la tabla está vacía (bloqueante breve para asegurar disponibilidad)
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            val dao = instance!!.productDao()
                            val count = dao.count()
                            // Si hay menos de 13, insertamos los 13 (REPLACE actualizará los existentes)
                            if (count < 13) {
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
                                        ),
                                        Product(
                                            id = 4,
                                            name = "Grand Theft Auto VI",
                                            price = 69.99,
                                            description = "El esperado regreso de la saga de mundo abierto de Rockstar.",
                                            imageResId = R.drawable.gta_vi,
                                            stock = 8
                                        ),
                                        Product(
                                            id = 5,
                                            name = "Call of Duty: Black Ops 6",
                                            price = 69.99,
                                            description = "Shooter en primera persona con campaña y multijugador intensos.",
                                            imageResId = R.drawable.cod_bo6,
                                            stock = 15
                                        ),
                                        Product(
                                            id = 6,
                                            name = "EA Sports FC 25",
                                            price = 69.99,
                                            description = "Fútbol de nueva generación con mejoras en Ultimate Team.",
                                            imageResId = R.drawable.ea_fc25,
                                            stock = 20
                                        ),
                                        Product(
                                            id = 7,
                                            name = "Helldivers 2",
                                            price = 39.99,
                                            description = "Acción cooperativa intensa para defender la Super Tierra.",
                                            imageResId = R.drawable.helldivers2,
                                            stock = 7
                                        ),
                                        Product(
                                            id = 8,
                                            name = "Baldur's Gate 3",
                                            price = 59.99,
                                            description = "RPG por turnos aclamado, basado en D&D.",
                                            imageResId = R.drawable.baldurs_gate_3,
                                            stock = 9
                                        ),
                                        Product(
                                            id = 9,
                                            name = "Starfield",
                                            price = 69.99,
                                            description = "Aventura espacial de Bethesda con exploración de galaxias.",
                                            imageResId = R.drawable.starfield,
                                            stock = 6
                                        ),
                                        Product(
                                            id = 10,
                                            name = "Dragon's Dogma 2",
                                            price = 69.99,
                                            description = "ARPG de mundo abierto con peones y combates dinámicos.",
                                            imageResId = R.drawable.dragons_dogma_2,
                                            stock = 10
                                        ),
                                        Product(
                                            id = 11,
                                            name = "Alan Wake 2",
                                            price = 49.99,
                                            description = "Thriller de horror psicológico con narrativa dual.",
                                            imageResId = R.drawable.alan_wake_2,
                                            stock = 4
                                        ),
                                        Product(
                                            id = 12,
                                            name = "Assassin's Creed Mirage",
                                            price = 49.99,
                                            description = "Sigilo y parkour en Bagdad, regreso a las raíces de la saga.",
                                            imageResId = R.drawable.ac_mirage,
                                            stock = 11
                                        ),
                                        Product(
                                            id = 13,
                                            name = "Resident Evil 4 Remake",
                                            price = 39.99,
                                            description = "Reinvención del clásico survival horror de Capcom.",
                                            imageResId = R.drawable.re4_remake,
                                            stock = 13
                                        )
                                    )
                                )
                            }
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


