package com.example.moviesapp.data.repository

import com.example.moviesapp.core.AUTHORIZATION_BEARER_TOKEN
import com.example.moviesapp.core.BASE_URL
import com.example.moviesapp.core.Result
import com.example.moviesapp.data.MOVIE_DETAIL_PATH
import com.example.moviesapp.data.model.MovieDetailsResponse
import com.example.moviesapp.domain.repository.MovieDetailRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom

class MovieDetailRepositoryImpl(private val httpClient: HttpClient) : MovieDetailRepository {

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {

        return httpClient.get {
            headers {
                endPoint(MOVIE_DETAIL_PATH)
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    private fun HttpRequestBuilder.endPoint(path: String) {
        url {
            takeFrom(BASE_URL)
            encodedPath = path
            headers {
                append(
                    HttpHeaders.Authorization, AUTHORIZATION_BEARER_TOKEN
                )
            }
        }
    }
}