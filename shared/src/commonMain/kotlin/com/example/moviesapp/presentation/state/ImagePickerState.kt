package com.example.moviesapp.presentation.state

import com.example.moviesapp.domain.model.ImageData

sealed class ImagePickerState {
    object Idle : ImagePickerState()
    object Loading : ImagePickerState()
    data class Success(val image: ImageData) : ImagePickerState()
    data class Error(val message: String) : ImagePickerState()
}