package com.example.moviesapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.moviesapp.R
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalRadiusProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider

@Composable
fun MovieCarousel(
    movies: List<Movie>,
    onNavigateToMovieDetail: (Movie) -> Unit,
) {
    val spacingProvider = LocalSpacingProvider.current
    val pagerState = rememberPagerState(pageCount = {
        movies.count()
    })

    Column {
        HorizontalPager(
            pageSpacing = spacingProvider.spacing_4,
            state = pagerState
        ) { currentPage ->
            CarouselItem(movie = movies[currentPage], onNavigateToMovieDetail = onNavigateToMovieDetail)
        }
        Indicator(pagerState)
    }
}

@Composable
private fun CarouselItem(
    movie: Movie,
    onNavigateToMovieDetail: (Movie) -> Unit = {}
) {

    val spacingProvider = LocalSpacingProvider.current
    val typographyProvider = LocalTypographyProvider.current
    val radiusProvider = LocalRadiusProvider.current
    val colorProvider = LocalColorProvider.current

    Box(
        Modifier
            .height(190.dp)
            .width(400.dp)
            .clip(RoundedCornerShape(radiusProvider.large))
            .clickable {
                onNavigateToMovieDetail(movie)
            }
    ) {
        AsyncImage(
            model = movie.posterImage,
            contentScale = ContentScale.Crop,
            contentDescription = movie.title,
            error = painterResource(R.drawable.ic_placeholder)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(spacingProvider.spacing_6)
        ) {
            Text(
                movie.title,
                color = colorProvider.textColor.text_white,
                style = typographyProvider.bodyLarge
            )
        }
    }
}

@Composable
private fun Indicator(pagerState: PagerState) {

    val colorProvider = LocalColorProvider.current
    val spacingProvider = LocalSpacingProvider.current

    LazyRow(
        modifier = Modifier
            .padding(top = spacingProvider.spacing_2)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            count = pagerState.pageCount,
            key = { it }
        ) {
            val color = when {
                pagerState.currentPage == it -> colorProvider.backgroundColor.bg_week
                else -> colorProvider.backgroundColor.bg_strong
            }

            val size = if (pagerState.currentPage == it) 10.dp else 8.dp

            Box(
                modifier = Modifier
                    .padding(spacingProvider.spacing_1)
                    .background(color, CircleShape)
                    .size(size)
            )
        }
    }
}

@Preview
@Composable
fun MovieCarouselPreview() {
    MovieCarousel(
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
            ),
        ),
        onNavigateToMovieDetail = {}
    )
}
