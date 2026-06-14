package com.example.moviesapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.moviesapp.domain.model.ProfileData
import com.example.moviesapp.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ProfileRepository {

    companion object {
        private val FIRST_NAME = stringPreferencesKey("first_name")
        private val LAST_NAME = stringPreferencesKey("last_name")
        private val EMAIL = stringPreferencesKey("email")
        private val PHONE_NUMBER = stringPreferencesKey("phone_number")
        private val IMAGE_URL = stringPreferencesKey("image_url")
    }

    override fun getProfile(): Flow<ProfileData?> {
        return dataStore.data.map { preferences ->
            val firstName = preferences[FIRST_NAME]
            val lastName = preferences[LAST_NAME]
            val email = preferences[EMAIL]
            val phoneNumber = preferences[PHONE_NUMBER]
            val imageUrl = preferences[IMAGE_URL]

            if (lastName != null || email != null || phoneNumber != null || imageUrl != null) {
                ProfileData(
                    firstName = firstName,
                    lastName = lastName.orEmpty(),
                    email = email.orEmpty(),
                    phoneNumber = phoneNumber.orEmpty(),
                    imageUrl = imageUrl.orEmpty()
                )
            } else {
                null
            }
        }
    }

    override suspend fun saveProfile(profileData: ProfileData) {
        dataStore.edit { preferences ->
            profileData.firstName?.let { preferences[FIRST_NAME] = it }
            preferences[LAST_NAME] = profileData.lastName
            preferences[EMAIL] = profileData.email
            preferences[PHONE_NUMBER] = profileData.phoneNumber
            preferences[IMAGE_URL] = profileData.imageUrl
        }
    }

    override suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
