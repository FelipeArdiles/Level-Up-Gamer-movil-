package com.example.level_up_gamer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

/**
 * Utilidad para gestionar imágenes cargadas desde el dispositivo.
 * Guarda las imágenes en el almacenamiento interno de la app.
 */
object ImageManager {
    
    private const val IMAGE_DIR = "product_images"
    
    /**
     * Guarda una imagen desde un URI en el almacenamiento interno de la app.
     * @return La ruta relativa del archivo guardado, o null si falla.
     */
    suspend fun saveImageFromUri(context: Context, imageUri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            inputStream?.use { stream ->
                // Crear directorio si no existe
                val imageDir = File(context.filesDir, IMAGE_DIR)
                if (!imageDir.exists()) {
                    imageDir.mkdirs()
                }
                
                // Generar nombre único para la imagen
                val fileName = "product_${UUID.randomUUID()}.jpg"
                val imageFile = File(imageDir, fileName)
                
                // Leer y comprimir la imagen
                val bitmap = BitmapFactory.decodeStream(stream)
                val compressedBitmap = compressBitmap(bitmap, maxSizeKB = 500) // Comprimir a máximo 500KB
                
                // Guardar la imagen comprimida
                FileOutputStream(imageFile).use { out ->
                    compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }
                
                // Retornar ruta relativa (solo el nombre del archivo dentro del directorio)
                return@withContext "$IMAGE_DIR/$fileName"
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Obtiene el archivo de imagen desde una ruta relativa.
     */
    fun getImageFile(context: Context, imagePath: String?): File? {
        if (imagePath.isNullOrBlank()) return null
        val file = File(context.filesDir, imagePath)
        return if (file.exists()) file else null
    }
    
    /**
     * Obtiene el URI del archivo de imagen para usar con FileProvider.
     */
    fun getImageUri(context: Context, imagePath: String?): Uri? {
        val file = getImageFile(context, imagePath) ?: return null
        return androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    /**
     * Elimina una imagen del almacenamiento.
     */
    suspend fun deleteImage(context: Context, imagePath: String?): Boolean = withContext(Dispatchers.IO) {
        if (imagePath.isNullOrBlank()) return@withContext false
        try {
            val file = File(context.filesDir, imagePath)
            if (file.exists()) {
                file.delete()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Comprime un Bitmap para reducir su tamaño en KB.
     */
    private fun compressBitmap(bitmap: Bitmap, maxSizeKB: Int): Bitmap {
        var quality = 85
        var compressedBitmap = bitmap
        
        // Si la imagen es muy grande, redimensionarla primero
        val maxDimension = 1200
        if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
            val scale = minOf(
                maxDimension.toFloat() / bitmap.width,
                maxDimension.toFloat() / bitmap.height
            )
            val newWidth = (bitmap.width * scale).toInt()
            val newHeight = (bitmap.height * scale).toInt()
            compressedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        }
        
        return compressedBitmap
    }
}

