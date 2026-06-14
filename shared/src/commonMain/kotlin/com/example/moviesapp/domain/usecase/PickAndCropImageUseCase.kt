package com.example.moviesapp.domain.usecase

import com.example.moviesapp.ImageCropper
import com.example.moviesapp.PermissionManager
import com.example.moviesapp.domain.model.ImageData
import com.example.moviesapp.domain.repository.ImagePickerRepository

class PickAndCropImageUseCase(
    private val repository: ImagePickerRepository,
    private val permissionManager: PermissionManager,
    private val cropper: ImageCropper
) {

    suspend fun fromGallery(): ImageData? {
        if (!permissionManager.requestGalleryPermission()) return null
        val image = repository.pickFromGallery() ?: return null
        return cropper.crop(image)
    }

    suspend fun fromCamera(): ImageData? {
        if (!permissionManager.requestCameraPermission()) return null
        val image = repository.captureFromCamera() ?: return null
        return cropper.crop(image)
    }
}