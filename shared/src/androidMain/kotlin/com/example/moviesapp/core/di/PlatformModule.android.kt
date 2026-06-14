package com.example.moviesapp.core.di

import androidx.activity.ComponentActivity
import com.example.moviesapp.AndroidImageCropper
import com.example.moviesapp.ImageCropper
import com.example.moviesapp.ImagePicker
import com.example.moviesapp.PermissionManager
import com.example.moviesapp.core.datastore.provideDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory { (activity: ComponentActivity) -> PermissionManager(activity) }
    factory<ImageCropper> { (activity: ComponentActivity) -> AndroidImageCropper(activity) }
    factory { (activity: ComponentActivity) -> ImagePicker(activity) }
    single { provideDataStore(androidContext()) }
}
