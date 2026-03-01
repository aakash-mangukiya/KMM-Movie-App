package com.example.moviesapp.presentation.interactor

import com.example.moviesapp.core.onError
import com.example.moviesapp.core.onSuccess
import com.example.moviesapp.domain.model.MovieDetails
import com.example.moviesapp.domain.usecase.GetMovieDetailUseCase
import com.example.moviesapp.presentation.events.MovieDetailsEvent
import com.example.moviesapp.presentation.state.MovieDetailsStateHandler

class MovieDetailInteractor(
    private val stateHandler: MovieDetailsStateHandler,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) {

    fun subscribeToUiState() = stateHandler.uiState

    suspend fun processEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.LoadMovieDetails -> loadMovieDetails(event.movieId)
        }
    }

    private suspend fun loadMovieDetails(movieId: Int) {

        stateHandler.updateUiState {
            copy(isLoading = true)
        }

        getMovieDetailUseCase(movieId = movieId).onSuccess {
            stateHandler.updateUiState {
                copy(isLoading = false, movieDetails = MovieDetails(1, "Test Movie"))
            }
        }.onError { error ->
            stateHandler.updateUiState {
                copy(isLoading = false, error = error.message)
            }
        }
    }
}