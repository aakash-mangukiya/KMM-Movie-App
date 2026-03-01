package com.example.moviesapp.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

abstract class BaseStateHandler<UiState : BaseUiState>(initialState: UiState) {

    var currentState: UiState = initialState
    private val uiStateChannel = Channel<UiState>(Channel.UNLIMITED)

    val uiState: Flow<UiState> = uiStateChannel.receiveAsFlow()

    suspend fun updateUiState(update: UiState.() -> UiState) {
        currentState = currentState.update()
        withContext(Dispatchers.Main.immediate) {
            uiStateChannel.send(currentState)
        }
    }
}

abstract class BaseUiState