package com.example.moviesapp.presentation.events

sealed class MovieListEvent {

    object LoadMovies : MovieListEvent()

}