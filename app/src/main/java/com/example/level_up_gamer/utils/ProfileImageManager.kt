package com.example.level_up_gamer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ProfileImageManager {
    private const val PROFILE_IMAGES_DIR = "profile_images"
    private const val PROFILE_IMAGE_NAME = "profile_photo.jpg"

    /**
     * Obtiene el directorio donde se guardan las imágenes de perfil
     */
    private fun getProfileImagesDir(context: Context): File {
        val dir = File(context.filesDir, PROFILE_IMAGES_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Obtiene el archivo de imagen de perfil para un usuario específico
     */
    fun getProfileImageFile(context: Context, userId: String): File {
        val dir = getProfileImagesDir(context)
        return File(dir, "${userId}_$PROFILE_IMAGE_NAME")
    }

    /**
     * Guarda una imagen de perfil desde un Bitmap
     */
    suspend fun saveProfileImage(context: Context, userId: String, bitmap: Bitmap): String? {
        return withContext(Dispatchers.IO) {
            try {
                val imageFile = getProfileImageFile(context, userId)
                FileOutputStream(imageFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                imageFile.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Guarda una imagen de perfil desde un Uri
     */
    suspend fun saveProfileImageFromUri(context: Context, userId: String, uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap?.let { saveProfileImage(context, userId, it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Carga una imagen de perfil como Bitmap
     */
    suspend fun loadProfileImage(context: Context, imagePath: String?): Bitmap? {
        if (imagePath.isNullOrBlank()) return null
        
        return withContext(Dispatchers.IO) {
            try {
                val file = File(imagePath)
                if (file.exists()) {
                    BitmapFactory.decodeFile(file.absolutePath)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Elimina la imagen de perfil de un usuario
     */
    suspend fun deleteProfileImage(context: Context, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val imageFile = getProfileImageFile(context, userId)
                if (imageFile.exists()) {
                    imageFile.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Crea un Uri para compartir con la cámara usando FileProvider
     */
    fun createImageUri(context: Context, userId: String): Uri? {
        return try {
            val imageFile = getProfileImageFile(context, userId)
            // Si el archivo ya existe, eliminarlo para crear uno nuevo
            if (imageFile.exists()) {
                imageFile.delete()
            }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

