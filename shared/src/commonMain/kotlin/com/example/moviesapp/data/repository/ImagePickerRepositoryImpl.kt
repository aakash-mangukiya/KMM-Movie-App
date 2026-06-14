package com.example.moviesapp.data.repository

import com.example.moviesapp.ImagePicker
import com.example.moviesapp.domain.model.ImageData
import com.example.moviesapp.domain.repository.ImagePickerRepository

class ImagePickerRepositoryImpl(
    private val imagePicker: ImagePicker
) : ImagePickerRepository {

    override suspend fun pickFromGallery(): ImageData? {
        return imagePicker.pickImageFromGallery()
    }

    override suspend fun captureFromCamera(): ImageData? {
        return imagePicker.captureImageFromCamera()
    }
}