package com.example.moviesapp.data.repository

import com.example.moviesapp.core.Result
import com.example.moviesapp.data.MOVIE_LIST_PATH
import com.example.moviesapp.data.NOW_PLAYING_PATH
import com.example.moviesapp.data.mapper.toDomain
import com.example.moviesapp.data.model.MoviesResponse
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.repository.MovieListRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess

class MovieListRepositoryImpl(
    private val httpClient: HttpClient
) : MovieListRepository {

    override suspend fun getMovieList(page: Int): Result<List<Movie>?> {
        return try {
            val response = httpClient.get {
                headers {
                    endPoint(MOVIE_LIST_PATH)
                    parameter("page", page)
                }
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                Result.Success(response.body<MoviesResponse>().toDomain())
            } else {
                Result.Error(Exception("Error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getNowPlayingMovies(): Result<List<Movie>?> {
        return try {
            val response = httpClient.get {
                headers {
                    endPoint(NOW_PLAYING_PATH)
                }
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                Result.Success(response.body<MoviesResponse>().toDomain())
            } else {
                Result.Error(Exception("Error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun HttpRequestBuilder.endPoint(path: String) {
        url {
            encodedPath = path
        }
    }
}