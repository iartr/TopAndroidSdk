package com.offerfactory.topandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_NAME = "profile_name"
        private const val KEY_AGE = "profile_age"
        private const val KEY_GENRE_POSITION = "profile_genre_position"
        private const val KEY_ABOUT = "profile_about"
        private const val KEY_SAVE_ENABLED = "profile_save_enabled"
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _age = MutableLiveData<String>()
    val age: LiveData<String> = _age

    private val _genrePosition = MutableLiveData<Int>()
    val genrePosition: LiveData<Int> = _genrePosition

    private val _about = MutableLiveData<String>()
    val about: LiveData<String> = _about

    private val _saveEnabled = MutableLiveData<Boolean>(false)
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    init {
        restoreFromSavedState()

        loadSavedProfile()

        updateSaveEnabled()
    }

    private fun restoreFromSavedState() {
        savedStateHandle.get<String>(KEY_NAME)?.let { _name.value = it }
        savedStateHandle.get<String>(KEY_AGE)?.let { _age.value = it }
        savedStateHandle.get<Int>(KEY_GENRE_POSITION)?.let { _genrePosition.value = it }
        savedStateHandle.get<String>(KEY_ABOUT)?.let { _about.value = it }
        savedStateHandle.get<Boolean>(KEY_SAVE_ENABLED)?.let { _saveEnabled.value = it }
    }

    fun onNameChanged(newName: String) {
        _name.value = newName
        savedStateHandle[KEY_NAME] = newName
        updateSaveEnabled()
    }

    fun onAgeChanged(newAge: String) {
        _age.value = newAge
        savedStateHandle[KEY_AGE] = newAge
        updateSaveEnabled()
    }

    fun onGenreSelected(position: Int) {
        _genrePosition.value = position
        savedStateHandle[KEY_GENRE_POSITION] = position
        updateSaveEnabled()
    }

    fun onAboutChanged(newAbout: String) {
        _about.value = newAbout
        savedStateHandle[KEY_ABOUT] = newAbout
        updateSaveEnabled()
    }
    private fun updateSaveEnabled() {
        val isValid = isNameValid() && isAgeValid() && isAboutValid()
        _saveEnabled.value = isValid
        savedStateHandle[KEY_SAVE_ENABLED] = isValid
    }
    private fun isNameValid(): Boolean {
        val name = _name.value ?: ""
        return name.length in 2..40
    }
    private fun isAgeValid(): Boolean {
        val ageText = _age.value ?: ""
        val age = ageText.toIntOrNull() ?: return false
        return age in 10..120    }

    private fun isAboutValid(): Boolean {
        val about = _about.value ?: ""
        return about.isNotBlank()
    }
    private fun loadSavedProfile() {
        ProfileRepository.currentProfile?.let { profile ->
            _name.value = profile.name
            _age.value = profile.age.toString()
            _about.value = profile.about
            _genrePosition.value = 0


            savedStateHandle[KEY_NAME] = profile.name
            savedStateHandle[KEY_AGE] = profile.age.toString()
            savedStateHandle[KEY_ABOUT] = profile.about
            savedStateHandle[KEY_GENRE_POSITION] = 0

            updateSaveEnabled()
        }
    }

    fun saveProfile() {
        val name = _name.value ?: ""
        val age = _age.value?.toIntOrNull() ?: 0
        val genrePosition = _genrePosition.value ?: 0
        val about = _about.value ?: ""

        val genre = "Жанр $genrePosition"

        val profile = UserProfile(name, age, genre, about)
        ProfileRepository.currentProfile = profile
    }
}