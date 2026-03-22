package com.example.moviesapp.domain.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.domain.model.Movie

interface MovieListRepository {
    suspend fun getMovieList(page: Int): Result<List<Movie>?>
    suspend fun getNowPlayingMovies(): Result<List<Movie>?>
}