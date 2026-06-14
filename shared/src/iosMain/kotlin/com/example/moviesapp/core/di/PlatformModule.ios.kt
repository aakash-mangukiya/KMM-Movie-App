package com.example.moviesapp.core.di

import com.example.moviesapp.ImagePicker
import com.example.moviesapp.PermissionManager
import com.example.moviesapp.core.datastore.provideDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory { PermissionManager() }
    factory { ImagePicker() }
    single { provideDataStore() }
}
