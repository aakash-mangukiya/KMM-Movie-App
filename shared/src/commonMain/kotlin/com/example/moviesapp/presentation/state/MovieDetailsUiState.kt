package com.example.moviesapp.presentation.state

import com.example.moviesapp.core.BaseUiState
import com.example.moviesapp.domain.model.MovieDetails

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val error: String? = null
) : BaseUiState()