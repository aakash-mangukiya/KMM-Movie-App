package com.example.moviesapp.presentation.state

import com.example.moviesapp.core.BaseUiState
import com.example.moviesapp.domain.model.Movie

data class MovieUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
) : BaseUiState()