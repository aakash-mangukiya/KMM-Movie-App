package com.example.moviesapp.domain.usecase

import com.example.moviesapp.domain.model.ProfileData
import com.example.moviesapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke(): Flow<ProfileData?> {
        return repository.getProfile()
    }
}
