package com.example.moviesapp.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.moviesapp.core.BaseViewModel
import com.example.moviesapp.presentation.events.MovieListEvent
import com.example.moviesapp.presentation.events.ProfileEvent
import com.example.moviesapp.presentation.interactor.MovieListInteractor
import com.example.moviesapp.presentation.interactor.ProfileInteractor
import com.example.moviesapp.presentation.state.MovieUiState
import com.example.moviesapp.presentation.state.ProfileUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val interactor: ProfileInteractor
) : BaseViewModel<ProfileEvent>() {

    val uiState = interactor.subscribeToUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )

    init {
        handleEvent(ProfileEvent.LoadProfile)
    }

    override fun handleEvent(event: ProfileEvent) {
        viewModelScope.launch {
            interactor.processEvent(event)
        }
    }
}