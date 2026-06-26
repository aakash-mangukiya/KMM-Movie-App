package com.example.moviesapp

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.example.moviesapp.domain.model.ImageData
import java.io.File

fun Context.toImageData(uri: Uri): ImageData {
    var name: String? = null
    var size: Long? = null
    var mime: String? = null

    if (uri.scheme == "content") {
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)

            if (it.moveToFirst()) {
                if (nameIndex != -1) name = it.getString(nameIndex)
                if (sizeIndex != -1) size = it.getLong(sizeIndex)
            }
        }
        mime = contentResolver.getType(uri)
    } else if (uri.scheme == "file") {
        val file = File(uri.path ?: "")
        name = file.name
        size = file.length()
    }

    return ImageData(
        uri = uri.toString(),
        fileName = name,
        mimeType = mime,
        size = size
    )
}

fun createTempImageUri(context: Context): Uri {
    val file = File.createTempFile("camera_", ".jpg", context.filesDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}