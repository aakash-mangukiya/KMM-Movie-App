package com.example.moviesapp.presentation.interactor

import com.example.moviesapp.domain.model.ProfileData
import com.example.moviesapp.domain.usecase.GetProfileUseCase
import com.example.moviesapp.domain.usecase.PickAndCropImageUseCase
import com.example.moviesapp.domain.usecase.SaveProfileUseCase
import com.example.moviesapp.presentation.events.ProfileEvent
import com.example.moviesapp.presentation.state.ProfileStateHandler
import kotlinx.coroutines.flow.collectLatest

class ProfileInteractor(
    private val stateHandler: ProfileStateHandler,
    private val pickAndCropImageUseCase: PickAndCropImageUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val saveProfileUseCase: SaveProfileUseCase
) {
    fun subscribeToUiState() = stateHandler.uiState

    suspend fun processEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfile -> {
                getProfileUseCase().collectLatest { profile ->
                    stateHandler.updateUiState {
                        copy(
                            isLoading = false,
                            profileData = profile
                        )
                    }
                }
            }

            is ProfileEvent.PickImageFromCamera -> {
                captureFromCamera()
            }

            is ProfileEvent.PickImageFromGallery -> {
                pickFromGallery()
            }

            is ProfileEvent.SaveProfile -> {
                saveProfileUseCase(event.profileData)
            }

            is ProfileEvent.UpdateFirstName -> {
                stateHandler.updateUiState {
                    copy(profileData = (profileData ?: createEmptyProfile()).copy(firstName = event.firstName))
                }
            }

            is ProfileEvent.UpdateLastName -> {
                stateHandler.updateUiState {
                    copy(profileData = (profileData ?: createEmptyProfile()).copy(lastName = event.lastName))
                }
            }

            is ProfileEvent.UpdateEmail -> {
                stateHandler.updateUiState {
                    copy(profileData = (profileData ?: createEmptyProfile()).copy(email = event.email))
                }
            }

            is ProfileEvent.UpdatePhoneNumber -> {
                stateHandler.updateUiState {
                    copy(profileData = (profileData ?: createEmptyProfile()).copy(phoneNumber = event.phoneNumber))
                }
            }
        }
    }

    private fun createEmptyProfile() = ProfileData(
        firstName = "",
        lastName = "",
        email = "",
        phoneNumber = "",
        imageUrl = ""
    )

    private suspend fun pickFromGallery() {
        val result = pickAndCropImageUseCase.fromGallery() ?: return
        stateHandler.updateUiState {
            copy(
                profileData = (profileData ?: createEmptyProfile()).copy(imageUrl = result.uri),
                imageData = result
            )
        }
    }

    private suspend fun captureFromCamera() {
        val result = pickAndCropImageUseCase.fromCamera() ?: return
        stateHandler.updateUiState {
            copy(
                profileData = (profileData ?: createEmptyProfile()).copy(imageUrl = result.uri),
                imageData = result
            )
        }
    }
}
