package com.example.moviesapp.presentation.di

import com.example.moviesapp.presentation.interactor.MovieDetailInteractor
import com.example.moviesapp.presentation.interactor.MovieListInteractor
import com.example.moviesapp.presentation.state.MovieDetailsStateHandler
import com.example.moviesapp.presentation.state.MovieListStateHandler
import com.example.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
    single { MovieListStateHandler() }
    factory { MovieDetailsStateHandler() }
    single { MovieListInteractor(get(), get()) }
    factory { MovieDetailInteractor(get(), get()) }
}