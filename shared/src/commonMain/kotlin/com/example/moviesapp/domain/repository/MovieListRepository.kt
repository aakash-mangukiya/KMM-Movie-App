package com.example.moviesapp.domain.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.data.model.MoviesResponse

interface MovieListRepository {

    suspend fun getMovieList() : Result<List<MoviesResponse>>
}