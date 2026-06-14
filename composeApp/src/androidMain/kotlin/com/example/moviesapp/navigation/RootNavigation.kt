package com.example.moviesapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.moviesapp.screen.MovieDetailsScreen
import com.example.moviesapp.screen.MovieListScreen
import com.example.moviesapp.screen.ProfileScreen

@Composable
fun RootNavigation() {
    val backStack = rememberNavBackStack(AppNavKey.MovieList)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(200),
                initialOffsetX = { fullWidth -> fullWidth }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(200),
                targetOffsetX = { fullWidth -> -fullWidth }
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                animationSpec = tween(200),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(200),
                targetOffsetX = { fullWidth -> fullWidth }
            )
        },
        entryProvider = entryProvider {
            entry<AppNavKey.MovieList> {
                MovieListScreen(
                    onNavigateToProfile = { backStack.add(AppNavKey.Profile) },
                    onNavigateToDetails = { movieId, movieName ->
                        backStack.add(AppNavKey.MovieDetails(movieId, movieName))
                    }
                )
            }
            entry<AppNavKey.MovieDetails> { key ->
                MovieDetailsScreen(
                    movieId = key.movieId,
                    movieName = key.movieName,
                    onNavigationBack = { backStack.removeLastOrNull() }
                )
            }
            entry<AppNavKey.Profile> {
                ProfileScreen(
                    onNavigationBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}