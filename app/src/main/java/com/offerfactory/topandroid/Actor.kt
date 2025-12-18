package com.offerfactory.topandroid

data class Actor(
    val id: Int,
    val name: String,
    val birthYear: Int,
    val country: String,
    val avatarUrl: String,
    val bio: String,
    val knownFor: List<String>,
    val wikiUrl: String
)
