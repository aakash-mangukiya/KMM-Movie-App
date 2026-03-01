package com.example.moviesapp.domain.usecase

import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.MovieDetailsResponse
import com.example.moviesapp.domain.repository.MovieDetailRepository

class GetMovieDetailUseCase(private val repository: MovieDetailRepository) {

    suspend operator fun invoke(movieId: Int): Result<MovieDetailsResponse> {
        return repository.getMovieDetails(movieId)
    }
}