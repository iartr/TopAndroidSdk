package com.offerfactory.topandroid

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val duration: String,
    val rating: Double,
    val description: String,
    val kinopoiskUrl: String
)
