package com.example.moviesapp.core

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Exception) : Result<Nothing>
}

inline fun <T> Result<T>.onSuccess(
    block: (T) -> Unit
): Result<T> = if (this is Result.Success) also { block(data) } else this

inline fun <T> Result<T>.onError(
    block: (Exception) -> Unit
): Result<T> = if (this is Result.Error) also { block(exception) } else this