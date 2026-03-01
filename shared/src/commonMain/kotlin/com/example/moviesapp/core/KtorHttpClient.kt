package com.example.moviesapp.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorHttpClient {

    fun createHttpClient() = HttpClient {

        expectSuccess = false

        install(HttpTimeout) {
            val timeout = 60000L
            connectTimeoutMillis = timeout
            requestTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }

        install(ResponseObserver) {
            onResponse { response ->
                println("AppDebug HTTP ResponseObserver status: ${response.status.value}")
            }
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("AppDebug KtorHttpClient message:$message")
                }
            }
        }

        install(ContentNegotiation) {
            json(Json {
                explicitNulls = false
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
                encodeDefaults = true
                classDiscriminator = "#class"
            })
        }
    }
}