package com.example.moviesapp.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    val tagLine: String,
    val image: String,
    val director: String,
    val writer: String,
    val cast: List<Cast>,
    val genres: String,
    val budget: Int,
    val revenue: Int,
    val releaseDate: String,
    val status: String,
    val runtime: Int,
    val similarMovies: List<Movie>
)

data class Cast(
    val character: String,
    val name: String,
    val profilePath: String
)