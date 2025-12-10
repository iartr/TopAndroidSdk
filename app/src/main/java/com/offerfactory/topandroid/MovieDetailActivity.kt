package com.offerfactory.topandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MovieDetailActivity"
        private const val KEY_MOVIE_ID = "movie_id"
    }

    private var currentMovie: Movie? = null

    private lateinit var titleTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var randomButton: Button
    private lateinit var moreInfoButton: Button
    private lateinit var profileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        setContentView(R.layout.activity_movie_detail)

        // Инициализация ВСЕХ View элементов ПЕРВЫМ делом
        titleTextView = findViewById(R.id.titleTextView)
        infoTextView = findViewById(R.id.infoTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        randomButton = findViewById(R.id.randomMovieButton)
        moreInfoButton = findViewById(R.id.moreInfoButton)
        profileButton = findViewById(R.id.profileButton)

        // Восстановление состояния
        if (savedInstanceState != null) {
            val savedMovieId = savedInstanceState.getInt(KEY_MOVIE_ID, -1)
            if (savedMovieId != -1) {
                currentMovie = MovieRepository.movies.find { it.id == savedMovieId }
                Log.d(TAG, "Restored movie from saved state: ${currentMovie?.title}")
            }
        }

        // Загрузка фильма если не восстановился
        if (currentMovie == null) {
            currentMovie = MovieRepository.getRandomMovie()
            Log.d(TAG, "First launch: loading random movie")
        }

        // Отображение фильма
        displayMovie(currentMovie)

        // Настройка обработчиков
        setupClickListeners()
        setupRandomButtonListener()
        setupProfileButtonListener()
    }

    private fun setupProfileButtonListener() {
        profileButton.setOnClickListener {
            Log.d(TAG, "Profile button clicked")
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayMovie(movie: Movie?) {
        movie?.let {
            Log.d(TAG, "Displaying movie: ${it.title}")

            titleTextView.text = it.title
            infoTextView.text = "${it.year} • ${it.genre} • ${it.duration}"
            descriptionTextView.text = it.description
            ratingTextView.text = it.rating.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")

        currentMovie?.let { movie ->
            outState.putInt(KEY_MOVIE_ID, movie.id)
            Log.d(TAG, "Saved movie ID: ${movie.id} (${movie.title})")
        }
    }

    private fun setupClickListeners() {
        // Кнопка "Подробнее" — открывает браузер с Kinopoisk
        moreInfoButton.setOnClickListener {
            Log.d(TAG, "More info button clicked")

            currentMovie?.let { movie ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.kinopoiskUrl))

                // Проверяем, что есть приложение для обработки Intent
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    Log.d(TAG, "Opening URL: ${movie.kinopoiskUrl}")
                } else {
                    // Нет приложения для обработки — показываем Toast
                    Toast.makeText(
                        this,
                        "Нет приложения для открытия ссылки",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.w(TAG, "No app found to handle URL")
                }
            }
        }
    }

    private fun getRandomMovie(): Movie {
        val allMovies = MovieRepository.movies

        if (currentMovie == null) {
            return allMovies.random()
        }

        val availableMovies = allMovies.filter { movie ->
            movie.id != currentMovie!!.id
        }

        return if (availableMovies.isNotEmpty()) {
            availableMovies.random()
        } else {
            allMovies.random()
        }
    }

    private fun setupRandomButtonListener() {
        randomButton.setOnClickListener {
            Log.d(TAG, "Random movie button clicked")

            val previousMovie = currentMovie?.title ?: "нет фильма"
            currentMovie = getRandomMovie()
            displayMovie(currentMovie)

            val newMovie = currentMovie?.title ?: "нет фильма"
            Log.d(TAG, "Смена фильма. $previousMovie → $newMovie")

            Toast.makeText(this, "Фильм изменен!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called - Activity is now visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - Activity is now interactive")
    }

    override fun onPause() {
        Log.d(TAG, "onPause called - Activity is losing focus")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop called - Activity is no longer visible")
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart called - Activity is being restarted")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called - Activity is being destroyed")
        super.onDestroy()
    }
}