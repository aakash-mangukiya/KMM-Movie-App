package com.example.moviesapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.moviesapp.core.BaseViewModel
import com.example.moviesapp.presentation.events.MovieListEvent
import com.example.moviesapp.presentation.interactor.MovieListInteractor
import com.example.moviesapp.presentation.state.MovieUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val interactor: MovieListInteractor
) : BaseViewModel<MovieListEvent>() {

    val uiState = interactor.subscribeToUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieUiState()
    )

    override fun handleEvent(event: MovieListEvent) {
        viewModelScope.launch {
            when (event) {
                is MovieListEvent.LoadMovies -> interactor.processEvent(event)
            }
        }
    }
}