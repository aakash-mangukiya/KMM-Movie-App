package com.example.moviesapp.domain.usecase

import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.MoviesResponse
import com.example.moviesapp.domain.repository.MovieListRepository

class GetMovieListUseCase(private val repository: MovieListRepository) {

    suspend operator fun invoke(): Result<List<MoviesResponse>> {
        return repository.getMovieList()
    }
}