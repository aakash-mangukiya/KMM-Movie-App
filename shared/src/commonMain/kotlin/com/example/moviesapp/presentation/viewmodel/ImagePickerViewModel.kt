package com.example.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.usecase.PickAndCropImageUseCase
import com.example.moviesapp.presentation.state.ImagePickerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImagePickerViewModel(
    private val useCase: PickAndCropImageUseCase
): ViewModel() {

    private val _state = MutableStateFlow<ImagePickerState>(ImagePickerState.Idle)
    val state: StateFlow<ImagePickerState> = _state

    fun pickFromGallery() {
        viewModelScope.launch {
            _state.value = ImagePickerState.Loading
            val result = useCase.fromGallery()
            _state.value = result?.let { ImagePickerState.Success(it) }
                ?: ImagePickerState.Error("Failed")
        }
    }

    fun captureFromCamera() {
        viewModelScope.launch {
            _state.value = ImagePickerState.Loading
            val result = useCase.fromCamera()
            _state.value = result?.let { ImagePickerState.Success(it) }
                ?: ImagePickerState.Error("Failed")
        }
    }
}