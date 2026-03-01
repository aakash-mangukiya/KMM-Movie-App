package com.example.moviesapp.presentation.di

import com.example.moviesapp.presentation.interactor.MovieDetailInteractor
import com.example.moviesapp.presentation.interactor.MovieListInteractor
import com.example.moviesapp.presentation.state.MovieDetailsStateHandler
import com.example.moviesapp.presentation.state.MovieListStateHandler
import org.koin.dsl.module

val interactorModule = module {
    single { MovieListStateHandler() }
    single { MovieDetailsStateHandler() }
    single { MovieListInteractor(get(), get()) }
    single { MovieDetailInteractor(get(), get()) }
}