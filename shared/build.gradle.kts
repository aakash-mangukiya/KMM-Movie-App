import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.skie)
}

// Read the local.properties file
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

val apiKey = localProperties.getProperty("API_KEY") ?: "default_value"

val generateSharedConfig by tasks.registering {
    val apiKeyProperty = objects.property<String>().convention(apiKey)
    val outputDirProperty = objects.directoryProperty().convention(layout.buildDirectory.dir("generated/config/src/commonMain/kotlin"))

    inputs.property("apiKey", apiKeyProperty)
    outputs.dir(outputDirProperty)

    doLast {
        val outDir = outputDirProperty.get().asFile
        val configFile = File(outDir, "SharedConfig.kt")
        configFile.parentFile.mkdirs()
        configFile.writeText("""
            package com.example.moviesapp.generated
            
            object SharedConfig {
                const val API_KEY = "${apiKeyProperty.get()}"
            }
        """.trimIndent())
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.skie.annotations)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    sourceSets {
        commonMain.configure {
            // Tell Kotlin to look at our generated folder
            kotlin.srcDir(generateSharedConfig.map { it.outputs.files.asPath })
        }
    }
}

android {
    namespace = "com.example.moviesapp.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

skie {
    features {
        // https://skie.touchlab.co/features/flows-in-swiftui
        enableSwiftUIObservingPreview = true
    }

    analytics {
        enabled.set(false)
    }
}
