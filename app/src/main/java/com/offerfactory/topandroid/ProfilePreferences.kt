package com.offerfactory.topandroid

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class ProfilePreferences(context: Context) {
    companion object {
        private const val PREFS_NAME = "profile_preferences"
        private const val KEY_PROFILE = "profile_data"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveProfile(profile: Profile) {
        val profileJson = gson.toJson(profile)
        prefs.edit().putString(KEY_PROFILE, profileJson).apply()
    }

    fun loadProfile(): Profile? {
        val profileJson = prefs.getString(KEY_PROFILE, null)
        return if (profileJson != null) {
            gson.fromJson(profileJson, Profile::class.java)
        } else {
            null
        }
    }
    //я хз icq упал не могу положить эту хуйню в активити
    fun loadDefaultProfile(): Profile {
        return Profile(
            name = "Гость",
            age = 0,
            genre = "Не указан",
            bio = ""
        )
    }

    fun clearProfile() {
        prefs.edit().remove(KEY_PROFILE).apply()
    }
}