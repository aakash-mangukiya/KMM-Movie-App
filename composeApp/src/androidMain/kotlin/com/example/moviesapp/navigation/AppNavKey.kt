package com.example.moviesapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppNavKey : NavKey {
    @Serializable
    data object MovieList : AppNavKey

    @Serializable
    data class MovieDetails(val movieId: Int, val movieName:String)
        : AppNavKey

    @Serializable
    data object Profile : AppNavKey
}

