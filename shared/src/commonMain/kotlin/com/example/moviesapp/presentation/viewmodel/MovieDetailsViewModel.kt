package com.example.moviesapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.moviesapp.core.BaseViewModel
import com.example.moviesapp.presentation.events.MovieDetailsEvent
import com.example.moviesapp.presentation.interactor.MovieDetailInteractor
import com.example.moviesapp.presentation.state.MovieUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val interactor: MovieDetailInteractor
) : BaseViewModel<MovieDetailsEvent>() {

    val uiState = interactor.subscribeToUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieUiState()
    )

    override fun handleEvent(event: MovieDetailsEvent) {
        viewModelScope.launch {
            when (event) {
                is MovieDetailsEvent.LoadMovieDetails -> interactor.processEvent(event)
            }
        }
    }
}