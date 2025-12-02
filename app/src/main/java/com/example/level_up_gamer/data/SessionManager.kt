package com.example.level_up_gamer.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestor de sesión del usuario actual.
 * Almacena y recupera el ID del usuario autenticado usando SharedPreferences.
 */
object SessionManager {
    private const val PREFS_NAME = "levelupgamer_session"
    private const val KEY_USER_ID = "current_user_id"
    
    private var prefs: SharedPreferences? = null
    
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }
    
    /**
     * Guarda el ID del usuario actual en la sesión
     */
    fun setCurrentUserId(userId: String?) {
        prefs?.edit()?.apply {
            if (userId != null) {
                putString(KEY_USER_ID, userId)
            } else {
                remove(KEY_USER_ID)
            }
            apply()
        }
    }
    
    /**
     * Obtiene el ID del usuario actual de la sesión
     */
    fun getCurrentUserId(): String? {
        return prefs?.getString(KEY_USER_ID, null)
    }
    
    /**
     * Verifica si hay un usuario autenticado
     */
    fun isLoggedIn(): Boolean {
        return getCurrentUserId() != null
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        setCurrentUserId(null)
    }
}

