package com.example.moviesapp.di

import com.example.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
}