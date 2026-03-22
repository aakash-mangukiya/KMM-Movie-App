@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.moviesapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import com.example.moviesapp.R
import com.example.moviesapp.components.CastItem
import com.example.moviesapp.components.ErrorView
import com.example.moviesapp.components.InfoView
import com.example.moviesapp.components.LoaderIndicator
import com.example.moviesapp.components.MovieVerticalItem
import com.example.moviesapp.domain.model.Cast
import com.example.moviesapp.domain.model.MovieDetails
import com.example.moviesapp.presentation.events.MovieDetailsEvent
import com.example.moviesapp.presentation.state.MovieDetailsUiState
import com.example.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider
import com.example.moviesapp.theme.MovieAppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    movieName: String,
    onNavigationBack: () -> Unit,
    viewModel: MovieDetailsViewModel = koinViewModel()
) {

    BackHandler {
        onNavigationBack()
    }

    val uiState = viewModel.uiState.collectAsState().value
    LaunchedEffect(movieId) {
        viewModel.handleEvent(
            event = MovieDetailsEvent.LoadMovieDetails(movieId = movieId)
        )
    }

    MovieDetails(
        uiState = uiState,
        movieName = movieName,
        onNavigationBack = onNavigationBack,
        onRetry = {
            viewModel.handleEvent(
                event = MovieDetailsEvent.LoadMovieDetails(movieId = movieId)
            )
        }
    )
}

@Composable
private fun MovieDetails(
    uiState: MovieDetailsUiState,
    movieName: String,
    onNavigationBack: () -> Unit,
    onRetry: () -> Unit = {},
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
                        text = movieName,
                        style = typographyProvider.titleMedium,
                        color = colorProvider.textColor.text_white
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorProvider.backgroundColor.bg_strong
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigationBack) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            tint = colorProvider.backgroundColor.bg_white,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                LoaderIndicator(
                    color = colorProvider.backgroundColor.bg_strong
                )
            }

            uiState.error.isNullOrEmpty().not() -> {
                ErrorView(
                    error = uiState.error
                        ?: "An error occurred while fetching movie details. Please try again.",
                    onRetry = { onRetry() }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .fillMaxWidth()
                ) {

                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        val (img, title, tagline, description, director, writer, txtCast, cast, rate, status) = createRefs()
                        val (revenue, budget, txtSimilarMovie, similarMovies) = createRefs()
                        val guideline = createGuidelineFromStart(0.5f)

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .constrainAs(img) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            model = uiState.movieDetails?.image,
                            contentDescription = uiState.movieDetails?.title,
                            contentScale = ContentScale.Crop,
                            error = painterResource(R.drawable.ic_placeholder)
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(title) {
                                    top.linkTo(img.bottom, margin = spacingProvider.spacing_2)
                                    start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                    end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                    width = Dimension.fillToConstraints
                                },
                            text = uiState.movieDetails?.title.orEmpty(),
                            style = typographyProvider.titleLarge,
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(tagline) {
                                    top.linkTo(title.bottom, margin = spacingProvider.spacing_2)
                                    start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                    end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                    width = Dimension.fillToConstraints
                                },
                            text = uiState.movieDetails?.tagLine.orEmpty(),
                            style = typographyProvider.headLine.copy(fontStyle = FontStyle.Italic),
                            color = colorProvider.textColor.text_medium
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(description) {
                                    top.linkTo(tagline.bottom, margin = spacingProvider.spacing_2)
                                    start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                    end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                    width = Dimension.fillToConstraints
                                },
                            text = uiState.movieDetails?.overview.orEmpty(),
                            style = typographyProvider.bodyMedium,
                            overflow = TextOverflow.Ellipsis,
                        )

                        InfoView(
                            modifier = Modifier.constrainAs(director) {
                                top.linkTo(description.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                end.linkTo(guideline, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Director",
                            value = uiState.movieDetails?.director.orEmpty(),
                        )

                        InfoView(
                            modifier = Modifier.constrainAs(writer) {
                                top.linkTo(description.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(guideline)
                                end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Writer",
                            value = uiState.movieDetails?.writer.orEmpty(),
                        )

                        Text(
                            modifier = Modifier.constrainAs(txtCast) {
                                top.linkTo(director.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            text = "Cast: ",
                            style = typographyProvider.subHeadLine,
                            overflow = TextOverflow.Ellipsis,
                        )

                        LazyRow(
                            modifier = Modifier.constrainAs(cast) {
                                top.linkTo(txtCast.bottom, margin = spacingProvider.spacing_1)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                            contentPadding = PaddingValues(all = spacingProvider.spacing_2),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space = spacingProvider.spacing_4)
                        ) {
                            items(
                                items = uiState.movieDetails?.cast ?: emptyList()
                            ) { cast ->
                                CastItem(cast = cast)
                            }
                        }

                        InfoView(
                            modifier = Modifier.constrainAs(rate) {
                                top.linkTo(cast.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                end.linkTo(guideline, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Rate",
                            value = "${uiState.movieDetails?.voteAverage} / 10"
                        )

                        InfoView(
                            modifier = Modifier.constrainAs(status) {
                                top.linkTo(cast.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(guideline)
                                end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Status",
                            value = uiState.movieDetails?.status.orEmpty()
                        )

                        InfoView(
                            modifier = Modifier.constrainAs(revenue) {
                                top.linkTo(rate.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                end.linkTo(guideline, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Revenue",
                            value = "${uiState.movieDetails?.revenue}"
                        )

                        InfoView(
                            modifier = Modifier.constrainAs(budget) {
                                top.linkTo(status.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(guideline)
                                end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            title = "Budget",
                            value = "${uiState.movieDetails?.budget}"
                        )

                        Text(
                            modifier = Modifier.constrainAs(txtSimilarMovie) {
                                top.linkTo(revenue.bottom, margin = spacingProvider.spacing_4)
                                start.linkTo(parent.start, margin = spacingProvider.spacing_2)
                                end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                                width = Dimension.fillToConstraints
                            },
                            text = "Similar Movies: ",
                            style = typographyProvider.subHeadLine,
                            overflow = TextOverflow.Ellipsis,
                        )

                        LazyRow(
                            modifier = Modifier.constrainAs(similarMovies) {
                                top.linkTo(
                                    txtSimilarMovie.bottom,
                                    margin = spacingProvider.spacing_1
                                )
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            },
                            contentPadding = PaddingValues(all = spacingProvider.spacing_2),
                            horizontalArrangement = Arrangement.spacedBy(space = spacingProvider.spacing_4)
                        ) {
                            items(
                                items = uiState.movieDetails?.similarMovies ?: emptyList(),
                                key = { movie -> "${movie.id}_${movie.title}" }
                            ) { movie ->
                                MovieVerticalItem(movie = movie, onNavigateToDetails = { _, _ -> })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMovieDetailScreen() {
    MovieAppTheme {
        MovieDetails(
            uiState = MovieDetailsUiState(
                movieDetails = MovieDetails(
                    id = 1234,
                    title = "Shelter",
                    overview = "A man living in self-imposed exile on a remote island rescues a young girl from a violent storm, setting off a chain of events that forces him out of retirement to protect her from enemies tied to his past.",
                    image = "",
                    director = "Ric Roman Waugh",
                    writer = "Ward Parry",
                    cast = listOf(
                        Cast(
                            name = "Jason Statham",
                            profilePath = "/pXGSq2UpcDE2NMF8LR56QZf5U1q.jpg",
                            character = "Mason"
                        ),
                        Cast(
                            name = "Michael Shaeffer",
                            profilePath = "/pXGSq2UpcDE2NMF8LR56QZf5U1q.jpg",
                            character = "Uncle"
                        )
                    ),
                    genres = "Action, Drama",
                    budget = 1000000,
                    revenue = 2000000,
                    releaseDate = "2023-05-15",
                    status = "Released",
                    runtime = 102,
                    voteAverage = 7.5,
                    tagLine = "Her safety. His mission.",
                    similarMovies = emptyList()
                )
            ),
            movieName = "Shelter",
            onNavigationBack = { }
        )
    }
}
