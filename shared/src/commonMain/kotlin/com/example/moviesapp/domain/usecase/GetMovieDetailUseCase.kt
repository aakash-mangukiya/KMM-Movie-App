package com.example.moviesapp.domain.usecase

import com.example.moviesapp.core.Result
import com.example.moviesapp.core.onError
import com.example.moviesapp.core.onSuccess
import com.example.moviesapp.domain.model.MovieDetails
import com.example.moviesapp.domain.repository.MovieDetailRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class GetMovieDetailUseCase(
    private val repository: MovieDetailRepository
) {

    suspend operator fun invoke(movieId: Int): Result<MovieDetails> {

        return supervisorScope {

            val movieDetailDeferred = async { repository.getMovieDetails(movieId) }
            val creditDetailDeferred = async { repository.getCredits(movieId) }
            val similarMoviesDeferred = async { repository.getSimilarMovies(movieId) }

            val movieDetailResult = movieDetailDeferred.await()
            val creditDetailResult = creditDetailDeferred.await()
            val similarMoviesResult = similarMoviesDeferred.await()

            if (similarMoviesResult is Result.Success && movieDetailResult is Result.Success && creditDetailResult is Result.Success) {
                val movieDetails = movieDetailResult.data
                val (cast, crew) = creditDetailResult.data
                val (director, writer) = crew
                val similarMovies = similarMoviesResult.data ?: emptyList()

                Result.Success(
                    movieDetails.copy(
                        cast = cast,
                        director = director,
                        writer = writer,
                        similarMovies = similarMovies
                    )
                )
            } else {
                Result.Error(Exception("Failed to fetch movie details"))
            }
        }
    }
}