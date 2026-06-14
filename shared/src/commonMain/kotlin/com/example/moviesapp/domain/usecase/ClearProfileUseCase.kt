package com.example.moviesapp.domain.usecase

import com.example.moviesapp.domain.repository.ProfileRepository

class ClearProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() {
        repository.clearProfile()
    }
}
