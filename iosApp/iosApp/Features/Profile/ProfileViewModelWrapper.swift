//
//  ProfileViewModelWrapper.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 19/04/26.
//

import SwiftUI
import Shared

@MainActor
class ProfileViewModelWrapper: ObservableObject {
    
    let profileViewModel: ProfileViewModel
    @Published var profileUiState: ProfileUiState
    
    init() {
        
        // Manually assemble dependencies to avoid Koin parameter issues on iOS
        let imagePicker = ImagePicker()
        let repository = ImagePickerRepositoryImpl(imagePicker: imagePicker)
        let permissionManager = PermissionManager()
        let cropper = IOSImageCropper()
        
        let dataStore = IosDataStoreKt.provideDataStore()
        let profileRepository = ProfileRepositoryImpl(dataStore: dataStore)
        let getProfileUseCase = GetProfileUseCase(repository: profileRepository)
        let saveProfileUseCase = SaveProfileUseCase(repository: profileRepository)
        
        let pickAndCropImageUseCase = PickAndCropImageUseCase(
            repository: repository,
            permissionManager: permissionManager,
            cropper: cropper
        )
        
        let stateHandler = ProfileStateHandler()
        let interactor = ProfileInteractor(
            stateHandler: stateHandler,
            pickAndCropImageUseCase: pickAndCropImageUseCase,
            getProfileUseCase: getProfileUseCase,
            saveProfileUseCase: saveProfileUseCase
        )
        
        self.profileViewModel = ProfileViewModel(interactor: interactor)
        self.profileUiState = ProfileUiState(isLoading: false, profileData: nil, imageData: nil, error: nil)
        
        Task { @MainActor in
            let sequence = SkieSwiftFlow(profileViewModel.uiState)
            for await state in sequence {
                self.profileUiState = state
            }
        }
    }
    
    func onEvent(_ event: ProfileEvent) {
        profileViewModel.handleEvent(event: event)
    }
    
    func loadProfile() {
        onEvent(ProfileEvent.LoadProfile())
    }
    
    func pickImageFromGallery() {
        onEvent(ProfileEvent.PickImageFromGallery())
    }
    
    func pickImageFromCamera() {
        onEvent(ProfileEvent.PickImageFromCamera())
    }
    
    func updateFirstName(firstName: String) {
        onEvent(ProfileEvent.UpdateFirstName(firstName: firstName))
    }
    
    func updateLastName(lastName: String) {
        onEvent(ProfileEvent.UpdateLastName(lastName: lastName))
    }
    
    func updateEmail(email: String) {
        onEvent(ProfileEvent.UpdateEmail(email: email))
    }
    
    func updatePhoneNumber(phoneNumber: String) {
        onEvent(ProfileEvent.UpdatePhoneNumber(phoneNumber: phoneNumber))
    }
    
    func saveProfile() {
        if let profileData = profileUiState.profileData {
            onEvent(ProfileEvent.SaveProfile(profileData: profileData))
        }
    }
}
