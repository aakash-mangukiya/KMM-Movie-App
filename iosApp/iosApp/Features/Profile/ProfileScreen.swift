//
//  ProfileScreen.swift
//  iosApp
//
//  Created by Aakash Mangukiya on 19/04/26.
//

import Foundation
import SwiftUI
import Shared
import UIKit

struct ProfileScreen: View {
    private enum ImageSource {
        case gallery
        case camera
    }
    
    @StateObject private var viewModel = ProfileViewModelWrapper()
    @State private var firstName = ""
    @State private var lastName = ""
    @State private var email = ""
    @State private var phoneNumber = ""
    @State private var isImageSourceSheetPresented = false
    @State private var pendingImageSource: ImageSource?
    
    var body: some View {
        
        VStack(spacing: 20) {
            
            Button {
                isImageSourceSheetPresented = true
            } label: {
                ZStack {
                    
                    Circle()
                        .stroke(Color.gray.opacity(0.4), lineWidth: 2)
                        .frame(width: 150, height: 150)
                    
                    let imagePath = viewModel.profileUiState.imageData?.uri ?? viewModel.profileUiState.profileData?.imageUrl ?? ""
                    let cleanPath = imagePath.replacingOccurrences(of: "file://", with: "")
                    
                    if let image = UIImage(contentsOfFile: cleanPath), !cleanPath.isEmpty {
                        Image(uiImage: image)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 150, height: 150)
                            .clipShape(Circle())
                    } else {
                        placeholderView()
                    }
                }
            }
            .buttonStyle(.plain)
            .padding(.top, 20)
            
            // Input Fields
            Group {
                
                CustomTextField(placeholder: "First Name", text: $firstName)
                    .onChange(of: firstName) { newValue in
                        viewModel.updateFirstName(firstName: newValue)
                    }
                
                CustomTextField(placeholder: "Last Name", text: $lastName)
                    .onChange(of: lastName) { newValue in
                        viewModel.updateLastName(lastName: newValue)
                    }
                
                CustomTextField(placeholder: "Email", text: $email)
                    .onChange(of: email) { newValue in
                        viewModel.updateEmail(email: newValue)
                    }
                
                CustomTextField(placeholder: "Phone Number", text: $phoneNumber)
                    .onChange(of: phoneNumber) { newValue in
                        viewModel.updatePhoneNumber(phoneNumber: newValue)
                    }
                
            }
            .padding(.horizontal, 20)
            
            // Save Button
            Button(action: {
                viewModel.saveProfile()
            }) {
                
                Text("SAVE")
                    .fontWeight(.semibold)
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.teal)
                    .cornerRadius(10)
                
            }
            .padding(.horizontal, 20)
            .padding(.top, 10)
            
            Spacer()
        }
        .task {
            viewModel.loadProfile()
            syncFormFields()
        }
        .onChange(of: viewModel.profileUiState.profileData) {
            syncFormFields()
        }
        .sheet(isPresented: $isImageSourceSheetPresented, onDismiss: handleImageSourceSelection) {
            imageSourceSheet.presentationDetents([.height(220)])
        }
        .navigationTitle("Profile")
        .navigationBarTitleDisplayMode(.inline)
    }
    
    private func syncFormFields() {
        guard let profileData = viewModel.profileUiState.profileData else { return }
        
        firstName = profileData.firstName ?? ""
        lastName = profileData.lastName
        email = profileData.email
        phoneNumber = profileData.phoneNumber
    }
    
    private var imageSourceSheet: some View {
        VStack(spacing: 16) {
            Text("Select Image Source")
                .font(.headline)
                .padding(.top, 20)
            
            Button("Gallery") {
                pendingImageSource = .gallery
                isImageSourceSheetPresented = false
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color.teal)
            .foregroundColor(.white)
            .cornerRadius(10)
            
            Button("Camera") {
                pendingImageSource = .camera
                isImageSourceSheetPresented = false
            }
            .frame(maxWidth: .infinity)
            .padding()
            .background(Color.gray.opacity(0.15))
            .foregroundColor(.black)
            .cornerRadius(10)
            
            Spacer()
        }
        .padding(.horizontal, 20)
    }
    
    private func handleImageSourceSelection() {
        guard let pendingImageSource else { return }
        
        self.pendingImageSource = nil
        
        switch pendingImageSource {
        case .gallery:
            viewModel.pickImageFromGallery()
        case .camera:
            viewModel.pickImageFromCamera()
        }
    }
    
    private func placeholderView() -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 20)
                .fill(Color.gray.opacity(0.12))
            
            Image(systemName: "person.crop.circle")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(.black)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 280)
    }
}

struct CustomTextField: View {
    
    var placeholder: String
    @Binding var text: String
    
    var body: some View {
        
        TextField(placeholder, text: $text)
            .padding()
            .background(Color.white)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(Color.gray.opacity(0.5), lineWidth: 1)
            )
            .cornerRadius(8)
    }
}
