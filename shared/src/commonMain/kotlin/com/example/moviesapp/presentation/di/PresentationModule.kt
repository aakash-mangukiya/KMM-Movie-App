package com.example.moviesapp.presentation.di

import com.example.moviesapp.presentation.interactor.MovieDetailInteractor
import com.example.moviesapp.presentation.interactor.MovieListInteractor
import com.example.moviesapp.presentation.interactor.ProfileInteractor
import com.example.moviesapp.presentation.state.MovieDetailsStateHandler
import com.example.moviesapp.presentation.state.MovieListStateHandler
import com.example.moviesapp.presentation.state.ProfileStateHandler
import com.example.moviesapp.presentation.viewmodel.ImagePickerViewModel
import com.example.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import com.example.moviesapp.presentation.viewmodel.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val presentationModule = module {

    viewModel { MovieListViewModel(interactor = get()) }
    single { MovieListStateHandler() }
    single {
        MovieListInteractor(
            stateHandler = get(),
            getMovieListUseCase = get(),
            getNowPlayingMoviesListUseCase = get()
        )
    }

    viewModel { MovieDetailsViewModel(interactor = get()) }
    factory { MovieDetailsStateHandler() }
    factory { MovieDetailInteractor(stateHandler = get(), getMovieDetailUseCase = get()) }

    viewModel { (activity: Any) -> ProfileViewModel(interactor = get { parametersOf(activity) }) }
    single { ProfileStateHandler() }
    factory { (activity: Any) ->
        ProfileInteractor(
            stateHandler = get(),
            pickAndCropImageUseCase = get { parametersOf(activity) },
            getProfileUseCase = get(),
            saveProfileUseCase = get()
        )
    }
    factory { (activity: Any) -> ImagePickerViewModel(useCase = get { parametersOf(activity) }) }
}