package com.example.moviesapp.domain.di

import com.example.moviesapp.domain.usecase.GetMovieDetailUseCase
import com.example.moviesapp.domain.usecase.GetMovieListUseCase
import com.example.moviesapp.domain.usecase.GetNowPlayingMoviesListUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetMovieListUseCase(get()) }
    single { GetMovieDetailUseCase(get()) }
    single { GetNowPlayingMoviesListUseCase(get()) }
}