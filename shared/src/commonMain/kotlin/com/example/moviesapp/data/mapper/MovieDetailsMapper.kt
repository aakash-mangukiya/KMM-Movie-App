package com.example.moviesapp.data.mapper

import com.example.moviesapp.core.IMAGE_BASE_URL
import com.example.moviesapp.core.IMAGE_BASE_WIDTH_200_URL
import com.example.moviesapp.data.model.CreditDetailsResponse
import com.example.moviesapp.data.model.MovieDetailsResponse
import com.example.moviesapp.domain.model.Cast
import com.example.moviesapp.domain.model.MovieDetails

fun MovieDetailsResponse.toMovieDetailDomain(): MovieDetails {
    return MovieDetails(
        id = id ?: 0,
        title = title.orEmpty(),
        overview = overview.orEmpty(),
        voteAverage = voteAverage ?: 0.0,
        tagLine = tagline.orEmpty(),
        image = IMAGE_BASE_URL + backdropPath.orEmpty(),
        director = "",
        writer = "",
        cast = emptyList(),
        genres = genres?.joinToString(", ") { it?.name.orEmpty() }.orEmpty(),
        budget = budget ?: 0,
        revenue = revenue ?: 0,
        releaseDate = releaseDate.orEmpty(),
        status = status.orEmpty(),
        runtime = runtime ?: 0,
        similarMovies = emptyList()
    )
}

fun CreditDetailsResponse.toCastDomain(): List<Cast> {
    return cast?.map {
        Cast(
            character = it?.character.orEmpty(),
            name = it?.name.orEmpty(),
            profilePath = IMAGE_BASE_WIDTH_200_URL + it?.profilePath.orEmpty()
        )
    } ?: emptyList()
}

fun CreditDetailsResponse.getDirector(): String {
    return crew?.find { it?.job == "Director" }?.name.orEmpty()
}

fun CreditDetailsResponse.getWriter(): String {
    return crew?.find { it?.job == "Writer" }?.name.orEmpty()
}
