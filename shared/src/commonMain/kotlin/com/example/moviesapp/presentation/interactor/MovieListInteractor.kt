package com.example.moviesapp.presentation.interactor

import com.example.moviesapp.core.onError
import com.example.moviesapp.core.onSuccess
import com.example.moviesapp.domain.usecase.GetMovieListUseCase
import com.example.moviesapp.domain.usecase.GetNowPlayingMoviesListUseCase
import com.example.moviesapp.presentation.events.MovieListEvent
import com.example.moviesapp.presentation.state.MovieListStateHandler

class MovieListInteractor(
    private val stateHandler: MovieListStateHandler,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val getNowPlayingMoviesListUseCase: GetNowPlayingMoviesListUseCase
) {

    fun subscribeToUiState() = stateHandler.uiState

    suspend fun processEvent(event: MovieListEvent) {
        when (event) {
            is MovieListEvent.LoadMovies -> loadMovies()
            is MovieListEvent.LoadNextPage -> loadNextPage()
        }
    }

    private suspend fun loadMovies() {
        if (stateHandler.currentState.isLoading) return

        stateHandler.updateUiState {
            copy(isLoading = true, movies = emptyList(), currentPage = 1, isEndReached = false)
        }

        fetchMovies(1)
        fetchNowPlayingMovies()
    }

    private suspend fun loadNextPage() {
        val currentState = stateHandler.currentState
        if (currentState.isLoading || currentState.isEndReached) return

        val nextPage = currentState.currentPage + 1
        
        stateHandler.updateUiState {
            copy(isLoading = true)
        }

        fetchMovies(nextPage)
    }

    private suspend fun fetchMovies(page: Int) {
        getMovieListUseCase(page = page)
            .onSuccess { result ->
                val movies = result ?: emptyList()
                stateHandler.updateUiState {
                    copy(
                        isLoading = false,
                        movies = if (page == 1) movies else this.movies + movies,
                        currentPage = page,
                        isEndReached = movies.isEmpty()
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

    private suspend fun fetchNowPlayingMovies() {
        getNowPlayingMoviesListUseCase()
            .onSuccess { result ->
                val movies = result ?: emptyList()
                stateHandler.updateUiState {
                    copy(
                        isLoading = false,
                        nowPlayingMovies = movies.take(5),
                        isEndReached = movies.isEmpty()
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