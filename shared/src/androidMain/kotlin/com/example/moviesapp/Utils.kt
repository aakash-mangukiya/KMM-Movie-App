package com.example.moviesapp

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.example.moviesapp.domain.model.ImageData
import java.io.File

fun Context.toImageData(uri: Uri): ImageData {
    val cursor = contentResolver.query(uri, null, null, null, null)

    var name: String? = null
    var size: Long? = null

    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

        if (it.moveToFirst()) {
            name = it.getString(nameIndex)
            size = it.getLong(sizeIndex)
        }
    }

    val mime = contentResolver.getType(uri)

    return ImageData(
        uri = uri.toString(),
        fileName = name,
        mimeType = mime,
        size = size
    )
}

fun createTempImageUri(context: Context): Uri {
    val file = File.createTempFile("camera_", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}