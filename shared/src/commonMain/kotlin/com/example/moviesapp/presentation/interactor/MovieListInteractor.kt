package com.example.moviesapp.presentation.interactor

import com.example.moviesapp.core.onError
import com.example.moviesapp.core.onSuccess
import com.example.moviesapp.domain.usecase.GetMovieListUseCase
import com.example.moviesapp.presentation.events.MovieListEvent
import com.example.moviesapp.presentation.state.MovieListStateHandler

class MovieListInteractor(
    private val stateHandler: MovieListStateHandler,
    private val getMovieListUseCase: GetMovieListUseCase
) {

    fun subscribeToUiState() = stateHandler.uiState

    suspend fun processEvent(event: MovieListEvent) {
        when (event) {
            is MovieListEvent.LoadMovies -> loadMovies()
        }
    }

    private suspend fun loadMovies() {

        stateHandler.updateUiState {
            copy(isLoading = true)
        }

        getMovieListUseCase()
            .onSuccess {
                stateHandler.updateUiState {
                    copy(
                        isLoading = false,
                        movies = emptyList(),
                    )
                }
            }.onError { error ->
                stateHandler.updateUiState {
                    copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
    }
}