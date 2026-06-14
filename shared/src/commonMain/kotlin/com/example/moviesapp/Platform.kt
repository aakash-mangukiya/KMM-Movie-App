package com.example.moviesapp

import com.example.moviesapp.domain.model.ImageData

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class ImagePicker {
    suspend fun pickImageFromGallery(): ImageData?
    suspend fun captureImageFromCamera(): ImageData?
}

expect class PermissionManager {
    suspend fun requestCameraPermission(): Boolean
    suspend fun requestGalleryPermission(): Boolean
}

interface ImageCropper {
    suspend fun crop(image: ImageData): ImageData?
}