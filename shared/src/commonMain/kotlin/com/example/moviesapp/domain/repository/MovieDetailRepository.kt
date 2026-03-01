package com.example.moviesapp.domain.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.MovieDetailsResponse

interface MovieDetailRepository {

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>
}