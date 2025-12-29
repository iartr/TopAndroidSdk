package com.offerfactory.topandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MovieDetailViewModel(
    // savedStateHandle используется для сохранения состояния после смерти процесса
    // но даже если не использовать, при смене конфигурации все равно будет сохраняться состояние!!!
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_MOVIE_ID = "movie_id"
    }

    private val movieMutable = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = movieMutable

    private val greetingNameMutable = MutableLiveData<String?>()
    val greetingName: LiveData<String?> = greetingNameMutable

    init {
        val restoredMovieId = savedStateHandle.get<Int>(KEY_MOVIE_ID)
        val restoredMovie = restoredMovieId?.let(MovieRepository::findMovieById)
        val initialMovie = restoredMovie ?: MovieRepository.getRandomMovie()

        setMovie(initialMovie)
        refreshGreeting()
    }

    fun loadRandomMovie() {
        val currentMovieId = movieMutable.value?.id
        val nextMovie = currentMovieId?.let(MovieRepository::getRandomMovieExcluding)
            ?: MovieRepository.getRandomMovie()
        setMovie(nextMovie)
    }

    fun refreshGreeting() {
        greetingNameMutable.value = ProfileRepository.currentProfile?.name
    }

    private fun setMovie(movie: Movie) {
        savedStateHandle[KEY_MOVIE_ID] = movie.id
        movieMutable.value = movie
    }
}

