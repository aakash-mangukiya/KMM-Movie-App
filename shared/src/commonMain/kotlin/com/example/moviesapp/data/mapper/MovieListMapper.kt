package com.example.moviesapp.data.mapper

import com.example.moviesapp.core.IMAGE_BASE_URL
import com.example.moviesapp.core.IMAGE_BASE_WIDTH_200_URL
import com.example.moviesapp.data.model.MoviesResponse
import com.example.moviesapp.domain.model.Movie

fun MoviesResponse.toDomain(): List<Movie>? {
    return this.movies?.map { movie ->
        Movie(
            id = movie.id ?: 0,
            title = movie.title.orEmpty(),
            lang = movie.originalLanguage.orEmpty(),
            overview = movie.overview.orEmpty(),
            image = IMAGE_BASE_WIDTH_200_URL + movie.posterPath.orEmpty(),
            posterImage = IMAGE_BASE_URL + movie.backdropPath.orEmpty(),
            releaseDate = movie.releaseDate.orEmpty()
        )
    }
}