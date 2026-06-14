package com.example.moviesapp.domain.model

data class ProfileData(
    val firstName: String? = null,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val imageUrl: String
)
