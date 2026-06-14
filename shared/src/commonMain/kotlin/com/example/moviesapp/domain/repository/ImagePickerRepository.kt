package com.example.moviesapp.domain.repository

import com.example.moviesapp.domain.model.ImageData

interface ImagePickerRepository {
    suspend fun pickFromGallery(): ImageData?
    suspend fun captureFromCamera(): ImageData?
}