package com.example.moviesapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = uiState.value.movies,
            key = { it.id }
        ) {

        }
    }
}