package com.example.moviesapp.core.di

import com.example.moviesapp.core.KtorHttpClient
import com.example.moviesapp.data.di.dataModule
import com.example.moviesapp.domain.di.domainModule
import com.example.moviesapp.presentation.di.presentationModule
import com.example.moviesapp.presentation.viewmodel.ImagePickerViewModel
import com.example.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import com.example.moviesapp.presentation.viewmodel.ProfileViewModel
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

val appModule = module {
    single { KtorHttpClient.createHttpClient() }
}

expect val platformModule: Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            appModule,
            dataModule,
            domainModule,
            presentationModule,
            platformModule
        )
    }

fun Koin.getMovieListViewModel(): MovieListViewModel = get()
fun Koin.getMovieDetailsViewModel(): MovieDetailsViewModel = get()
fun Koin.getImagePickerViewModel(): ImagePickerViewModel = get()
fun Koin.getProfileViewModel(): ProfileViewModel = get()
fun getKoinInstance(): Koin = KoinPlatform.getKoin()
