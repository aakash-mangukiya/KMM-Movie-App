package com.example.moviesapp.core.di

import com.example.moviesapp.core.KtorHttpClient
import org.koin.dsl.module

val appModule = module {
    single { KtorHttpClient.createHttpClient() }
}