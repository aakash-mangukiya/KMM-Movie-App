package com.example.moviesapp.domain.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.domain.model.Cast
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.model.MovieDetails

interface MovieDetailRepository {

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>
    suspend fun getCredits(movieId: Int): Result<Pair<List<Cast>, Pair<String, String>>>
    suspend fun getSimilarMovies(movieId: Int): Result<List<Movie>?>
}