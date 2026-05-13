package com.example.hallisanthe.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {

    private const val MAX_DIMENSION = 1024
    private const val COMPRESS_QUALITY = 80

    suspend fun compress(context: Context, imageUri: Uri): Uri = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(imageUri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI: $imageUri")

        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        if (originalBitmap == null) {
            throw IllegalArgumentException("Failed to decode bitmap from URI: $imageUri")
        }

        val scaledBitmap = scaleBitmap(originalBitmap, MAX_DIMENSION)
        if (scaledBitmap != originalBitmap) {
            originalBitmap.recycle()
        }

        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        FileOutputStream(compressedFile).use { out ->
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, out)
        }
        scaledBitmap.recycle()

        Uri.fromFile(compressedFile)
    }

    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxDimension && height <= maxDimension) {
            return bitmap
        }

        val ratio = width.toFloat() / height.toFloat()
        val (newWidth, newHeight) = if (width > height) {
            maxDimension to (maxDimension / ratio).toInt()
        } else {
            (maxDimension * ratio).toInt() to maxDimension
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
