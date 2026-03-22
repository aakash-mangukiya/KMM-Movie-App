package com.example.moviesapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val lang:String,
    val overview: String,
    val image:String,
    val posterImage: String,
    val releaseDate:String,
)