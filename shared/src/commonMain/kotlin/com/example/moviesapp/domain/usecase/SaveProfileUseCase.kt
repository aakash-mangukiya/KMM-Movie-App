package com.example.moviesapp.domain.usecase

import com.example.moviesapp.domain.model.ProfileData
import com.example.moviesapp.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profileData: ProfileData) {
        repository.saveProfile(profileData)
    }
}
