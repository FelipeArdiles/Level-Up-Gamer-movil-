package com.example.level_up_gamer.utils

import com.example.level_up_gamer.model.User

object AdminUtils {
    private const val ADMIN_EMAIL = "test@gamer.com"
    
    /**
     * Verifica si un usuario es administrador
     */
    fun isAdmin(user: User?): Boolean {
        return user?.email == ADMIN_EMAIL
    }
    
    /**
     * Verifica si el email corresponde a un administrador
     */
    fun isAdminEmail(email: String?): Boolean {
        return email == ADMIN_EMAIL
    }
}

