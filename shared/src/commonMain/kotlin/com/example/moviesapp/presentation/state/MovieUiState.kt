package com.example.moviesapp.presentation.state

import com.example.moviesapp.core.BaseUiState
import com.example.moviesapp.domain.model.Movie

data class MovieUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val error: String? = null,
    val currentPage: Int = 1,
    val isEndReached: Boolean = false
) : BaseUiState()