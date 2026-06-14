package com.example.moviesapp.domain.repository

import com.example.moviesapp.domain.model.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<ProfileData?>
    suspend fun saveProfile(profileData: ProfileData)
    suspend fun clearProfile()
}
