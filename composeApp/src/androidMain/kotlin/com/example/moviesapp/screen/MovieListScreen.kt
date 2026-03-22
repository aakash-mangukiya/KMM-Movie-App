@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moviesapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import com.example.moviesapp.R
import com.example.moviesapp.components.ErrorView
import com.example.moviesapp.components.LoaderIndicator
import com.example.moviesapp.components.MovieCarousel
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.presentation.events.MovieListEvent
import com.example.moviesapp.presentation.state.MovieUiState
import com.example.moviesapp.presentation.viewmodel.MovieListViewModel
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalRadiusProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider
import com.example.moviesapp.theme.MovieAppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = koinViewModel(),
    onNavigateToDetails: (movieId: Int, movieName: String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    MovieList(
        uiState = uiState,
        onNavigateToDetails = onNavigateToDetails,
        onLoadNextPage = { viewModel.handleEvent(MovieListEvent.LoadNextPage) }
    )
}

@Composable
private fun MovieList(
    uiState: MovieUiState,
    onNavigateToDetails: (movieId: Int, movieName: String) -> Unit,
    onLoadNextPage: () -> Unit = {}
) {
    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val colorProvider = LocalColorProvider.current

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = colorProvider.backgroundColor.bg_white,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Movies",
                        style = typographyProvider.titleMedium,
                        color = colorProvider.textColor.text_white
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorProvider.backgroundColor.bg_strong
                )
            )
        }
    ) { padding ->

        when {
            uiState.isLoading && uiState.movies.isEmpty() -> {
                LoaderIndicator(
                    color = colorProvider.backgroundColor.bg_strong
                )
            }

            uiState.error.isNullOrEmpty().not() -> {
                ErrorView(
                    error = uiState.error
                        ?: "An error occurred while fetching movies. Please try again.",
                    onRetry = onLoadNextPage
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(all = spacingProvider.spacing_4),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(space = spacingProvider.spacing_4)
                ) {

                    item {
                        MovieCarousel(movies = uiState.nowPlayingMovies) { movie ->
                            onNavigateToDetails(movie.id, movie.title)
                        }
                    }

                    itemsIndexed(
                        items = uiState.movies,
                        key = { _, movie -> "${movie.id}_${movie.title}" }
                    ) { index, movie ->
                        if (index >= uiState.movies.size - 1 && !uiState.isLoading && !uiState.isEndReached) {
                            LaunchedEffect(Unit) {
                                onLoadNextPage()
                            }
                        }
                        MovieItem(movie = movie, onNavigateToDetails = onNavigateToDetails)
                    }

                    if (uiState.isLoading && uiState.movies.isNotEmpty()) {
                        item {
                            LoaderIndicator(
                                modifier = Modifier.padding(vertical = spacingProvider.spacing_4),
                                color = colorProvider.backgroundColor.bg_strong
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    onNavigateToDetails: (movieId: Int, movieName: String) -> Unit,
) {

    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val radiusProvider = LocalRadiusProvider.current
    val colorProvider = LocalColorProvider.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onNavigateToDetails(movie.id, movie.title)
            },
        shape = RoundedCornerShape(radiusProvider.large),
        colors = CardDefaults.cardColors(containerColor = colorProvider.backgroundColor.bg_white),
        elevation = CardDefaults.cardElevation(defaultElevation = spacingProvider.spacing_4)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (img, title, description) = createRefs()

            AsyncImage(
                modifier = Modifier
                    .width(110.dp)
                    .height(150.dp)
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                model = movie.image,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_placeholder)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(parent.top, margin = spacingProvider.spacing_2)
                        start.linkTo(img.end, margin = spacingProvider.spacing_2)
                        end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                        width = Dimension.fillToConstraints
                    },
                text = movie.title,
                style = typographyProvider.titleSmall,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(description) {
                        top.linkTo(title.bottom, margin = spacingProvider.spacing_2)
                        bottom.linkTo(img.bottom)
                        start.linkTo(img.end, margin = spacingProvider.spacing_2)
                        end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                text = movie.overview,
                style = typographyProvider.bodyMedium,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {

    MovieAppTheme {
        MovieList(
            uiState = MovieUiState(
                movies = listOf(
                    Movie(
                        id = 12344,
                        title = "Shelter",
                        lang = "en",
                        overview = "A man living in self-imposed exile on a remote island rescues a young girl from a violent storm, setting off a chain of events that forces him out of seclusion to protect her from enemies tied to his past.",
                        image = "",
                        posterImage = "",
                        releaseDate = "2026-01-28"
                    ),
                    Movie(
                        id = 12344,
                        title = "History of the World: Part I",
                        lang = "en",
                        overview = "An uproarious version of history that proves nothing is sacred – not even the Roman Empire, the French Revolution and the Spanish Inquisition.",
                        image = "",
                        posterImage = "",
                        releaseDate = "1981-06-12"
                    )
                )
            ),
            onNavigateToDetails = { _, _ -> }
        )
    }
}