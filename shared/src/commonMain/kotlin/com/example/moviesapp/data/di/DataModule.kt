package com.example.moviesapp.data.di

import com.example.moviesapp.data.repository.MovieDetailRepositoryImpl
import com.example.moviesapp.data.repository.MovieListRepositoryImpl
import com.example.moviesapp.domain.repository.MovieDetailRepository
import com.example.moviesapp.domain.repository.MovieListRepository
import org.koin.dsl.module

val dataModule = module {
    single<MovieListRepository> { MovieListRepositoryImpl(get()) }
    single<MovieDetailRepository> { MovieDetailRepositoryImpl(get()) }
}