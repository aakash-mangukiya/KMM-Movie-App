package com.example.moviesapp.core

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<BaseEvent> : ViewModel() {

    abstract fun handleEvent(event: BaseEvent)
}