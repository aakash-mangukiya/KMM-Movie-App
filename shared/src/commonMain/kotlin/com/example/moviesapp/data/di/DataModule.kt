package com.example.moviesapp.data.di

import com.example.moviesapp.data.repository.ImagePickerRepositoryImpl
import com.example.moviesapp.data.repository.MovieDetailRepositoryImpl
import com.example.moviesapp.data.repository.MovieListRepositoryImpl
import com.example.moviesapp.data.repository.ProfileRepositoryImpl
import com.example.moviesapp.domain.repository.ImagePickerRepository
import com.example.moviesapp.domain.repository.MovieDetailRepository
import com.example.moviesapp.domain.repository.MovieListRepository
import com.example.moviesapp.domain.repository.ProfileRepository
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val dataModule = module {
    single<MovieListRepository> { MovieListRepositoryImpl(get()) }
    single<MovieDetailRepository> { MovieDetailRepositoryImpl(get()) }
    factory<ImagePickerRepository> { (activity: Any) -> ImagePickerRepositoryImpl(get { parametersOf(activity) }) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
}
