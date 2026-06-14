package com.example.moviesapp.presentation.events

import com.example.moviesapp.domain.model.ProfileData

sealed class ProfileEvent {
    data object LoadProfile : ProfileEvent()
    data object PickImageFromCamera : ProfileEvent()
    data object PickImageFromGallery : ProfileEvent()
    data class SaveProfile(val profileData: ProfileData) : ProfileEvent()
    data class UpdateFirstName(val firstName: String) : ProfileEvent()
    data class UpdateLastName(val lastName: String) : ProfileEvent()
    data class UpdateEmail(val email: String) : ProfileEvent()
    data class UpdatePhoneNumber(val phoneNumber: String) : ProfileEvent()
}
