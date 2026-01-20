package com.offerfactory.topandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ProfileViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_GENRE_POSITION = "genre_position"
        private const val KEY_GENRE = "genre"
        private const val KEY_ABOUT = "about"
    }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _ageText = MutableLiveData<String>()
    val ageText: LiveData<String> = _ageText

    private val _genrePosition = MutableLiveData<Int>()
    val genrePosition: LiveData<Int> = _genrePosition

    private val _genre = MutableLiveData<String?>()
    val genre: LiveData<String?> = _genre

    private val _about = MutableLiveData<String>()
    val about: LiveData<String> = _about

    private val _greetingName = MutableLiveData<String?>()
    val greetingName: LiveData<String?> = _greetingName

    private val _isNameValid = MutableLiveData<Boolean>()
    val isNameValid: LiveData<Boolean> = _isNameValid

    private val _isAgeValid = MutableLiveData<Boolean>()
    val isAgeValid: LiveData<Boolean> = _isAgeValid

    private val _isGenreValid = MutableLiveData<Boolean>()
    val isGenreValid: LiveData<Boolean> = _isGenreValid

    private val _isSaveEnabled = MutableLiveData<Boolean>()
    val isSaveEnabled: LiveData<Boolean> = _isSaveEnabled

    init {
        val restoredName = savedStateHandle.get<String>(KEY_NAME)
        val restoredAge = savedStateHandle.get<String>(KEY_AGE)
        val restoredGenrePosition = savedStateHandle.get<Int>(KEY_GENRE_POSITION)
        val restoredGenre = savedStateHandle.get<String>(KEY_GENRE)
        val restoredAbout = savedStateHandle.get<String>(KEY_ABOUT)

        val profile = ProfileRepository.currentProfile

        _name.value = restoredName ?: profile?.name.orEmpty()
        _ageText.value = restoredAge ?: profile?.age?.toString().orEmpty()
        _genrePosition.value = restoredGenrePosition ?: 0
        _genre.value = restoredGenre ?: profile?.genre
        _about.value = restoredAbout ?: profile?.about.orEmpty()

        _greetingName.value = profile?.name

        recalculateValidation()
    }

    fun onNameChanged(name: String) {
        _name.value = name
        savedStateHandle[KEY_NAME] = name
        recalculateValidation()
    }

    fun onAgeChanged(ageText: String) {
        _ageText.value = ageText
        savedStateHandle[KEY_AGE] = ageText
        recalculateValidation()
    }

    fun onGenreSelected(position: Int, genre: String?) {
        _genrePosition.value = position
        _genre.value = genre
        savedStateHandle[KEY_GENRE_POSITION] = position
        savedStateHandle[KEY_GENRE] = genre
        recalculateValidation()
    }

    fun onAboutChanged(about: String) {
        _about.value = about
        savedStateHandle[KEY_ABOUT] = about
    }

    fun saveProfile(): UserProfile? {
        if (isSaveEnabled.value != true) return null

        val name = _name.value.orEmpty()
        val age = _ageText.value?.toIntOrNull() ?: return null
        val genre = _genre.value ?: return null
        val about = _about.value.orEmpty()

        val profile = UserProfile(
            name = name,
            age = age,
            genre = genre,
            about = about
        )
        ProfileRepository.currentProfile = profile
        _greetingName.value = profile.name
        return profile
    }

    private fun recalculateValidation() {
        val name = _name.value.orEmpty()
        val ageText = _ageText.value.orEmpty()
        val genrePosition = _genrePosition.value ?: 0
        val genre = _genre.value

        val nameValid = name.length in 2..40
        val ageValid = ageText.toIntOrNull()?.let { it in 10..120 } ?: false
        val genreValid = genrePosition != 0 && !genre.isNullOrBlank()

        _isNameValid.value = nameValid
        _isAgeValid.value = ageValid
        _isGenreValid.value = genreValid
        _isSaveEnabled.value = nameValid && ageValid && genreValid
    }
}
