package com.example.moviesapp

import android.app.Application
import com.example.moviesapp.core.di.appModule
import com.example.moviesapp.data.di.dataModule
import com.example.moviesapp.domain.di.domainModule
import com.example.moviesapp.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MoviesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MoviesApplication)
            modules(
                appModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}