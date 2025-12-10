package com.offerfactory.topandroid

/**
 * Репозиторий для работы с профилем пользователя.
 */
object ProfileRepository {
    var currentProfile: Profile? = null

    fun saveProfile(profile: Profile) {
        currentProfile = profile
    }

    fun getProfile(): Profile? {
        return currentProfile
    }

    fun loadDefaultProfile(): Profile {
        return MovieRepository.getUserProfile()
    }
}