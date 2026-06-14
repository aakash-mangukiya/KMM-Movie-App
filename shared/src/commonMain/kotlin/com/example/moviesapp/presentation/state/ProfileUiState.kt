package com.example.moviesapp.presentation.state

import com.example.moviesapp.core.BaseUiState
import com.example.moviesapp.domain.model.ImageData
import com.example.moviesapp.domain.model.ProfileData

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profileData: ProfileData? = null,
    val imageData: ImageData? = null,
    val error: String? = null,
) : BaseUiState()