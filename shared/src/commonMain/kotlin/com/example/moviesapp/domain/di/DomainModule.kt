package com.example.moviesapp.domain.di

import com.example.moviesapp.domain.usecase.ClearProfileUseCase
import com.example.moviesapp.domain.usecase.GetMovieDetailUseCase
import com.example.moviesapp.domain.usecase.GetMovieListUseCase
import com.example.moviesapp.domain.usecase.GetNowPlayingMoviesListUseCase
import com.example.moviesapp.domain.usecase.GetProfileUseCase
import com.example.moviesapp.domain.usecase.PickAndCropImageUseCase
import com.example.moviesapp.domain.usecase.SaveProfileUseCase
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val domainModule = module {
    single { GetMovieListUseCase(repository = get()) }
    single { GetMovieDetailUseCase(repository = get()) }
    single { GetNowPlayingMoviesListUseCase(repository = get()) }
    single { GetProfileUseCase(repository = get()) }
    single { SaveProfileUseCase(repository = get()) }
    single { ClearProfileUseCase(repository = get()) }
    factory { (activity: Any) ->
        PickAndCropImageUseCase(
            repository = get { parametersOf(activity) },
            permissionManager = get { parametersOf(activity) },
            cropper = get { parametersOf(activity) }
        )
    }
}
