package com.example.moviesapp.presentation.events

sealed class MovieDetailsEvent {
    data class LoadMovieDetails(val movieId: Int) : MovieDetailsEvent()
}