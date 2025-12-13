package com.offerfactory.topandroid

import android.content.Context

object ProfileRepository {
    private lateinit var profilePreferences: ProfilePreferences

    fun init(context: Context) {
        profilePreferences = ProfilePreferences(context)
    }

    fun saveProfile(profile: Profile) {
        profilePreferences.saveProfile(profile)
    }

    fun getProfile(): Profile? {
        return profilePreferences.loadProfile()
    }
}