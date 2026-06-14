package com.example.moviesapp.presentation.events

sealed class MovieListEvent {
    data object LoadMovies : MovieListEvent()
    data object LoadNextPage : MovieListEvent()
}