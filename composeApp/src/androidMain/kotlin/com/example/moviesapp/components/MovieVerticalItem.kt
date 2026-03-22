package com.example.moviesapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.theme.LocalColorProvider
import com.example.moviesapp.theme.LocalRadiusProvider
import com.example.moviesapp.theme.LocalSpacingProvider
import com.example.moviesapp.theme.LocalTypographyProvider
import com.example.moviesapp.theme.MovieAppTheme

@Composable
fun MovieVerticalItem(
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
            .width(110.dp)
            .padding(bottom = spacingProvider.spacing_1)
            .clickable {
                onNavigateToDetails(movie.id, movie.title)
            },
        shape = RoundedCornerShape(radiusProvider.large),
        colors = CardDefaults.cardColors(containerColor = colorProvider.backgroundColor.bg_white),
        elevation = CardDefaults.cardElevation(defaultElevation = spacingProvider.spacing_4)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (img, title) = createRefs()

            AsyncImage(
                modifier = Modifier
                    .width(110.dp)
                    .height(150.dp)
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                model = movie.image,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_placeholder)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        top.linkTo(img.bottom, margin = spacingProvider.spacing_2)
                        start.linkTo(img.start, margin = spacingProvider.spacing_2)
                        end.linkTo(parent.end, margin = spacingProvider.spacing_2)
                        bottom.linkTo(parent.bottom, margin = spacingProvider.spacing_2)
                        width = Dimension.fillToConstraints
                    },
                text = movie.title,
                style = typographyProvider.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieVerticalItemPreview() {
    MovieAppTheme {
        MovieVerticalItem(
            movie = Movie(
                id = 12344,
                title = "Shelter",
                lang = "en",
                overview = "A man living in self-imposed exile on a remote island rescues a young girl from a violent storm, setting off a chain of events that forces him out of seclusion to protect her from enemies tied to his past.",
                image = "",
                posterImage = "",
                releaseDate = "2026-01-28"
            ),
            onNavigateToDetails = { _, _ -> }
        )
    }
}