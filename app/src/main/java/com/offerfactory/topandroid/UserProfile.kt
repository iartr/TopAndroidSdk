package com.offerfactory.topandroid

/**
 * Модель данных профиля пользователя
 */
data class UserProfile(
    val name: String,
    val age: Int,
    val genre: String,
    val about: String
)