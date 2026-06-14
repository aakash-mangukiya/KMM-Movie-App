package com.example.moviesapp.domain.model

data class ImageData(
    val uri: String,
    val fileName: String? = null,
    val mimeType: String? = null,
    val size: Long? = null,
)