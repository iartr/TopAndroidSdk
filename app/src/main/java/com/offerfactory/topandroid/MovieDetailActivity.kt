package com.offerfactory.topandroid

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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

    // UI элементы
    private lateinit var greetingTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var randomButton: Button
    private lateinit var anotherMovieButton: Button
    private lateinit var profileButton: Button
    private lateinit var moreInfoButton: Button
    private lateinit var actorButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        setContentView(R.layout.activity_movie_detail)

        // Инициализация UI элементов
        greetingTextView = findViewById(R.id.greetingTextView)
        titleTextView = findViewById(R.id.titleTextView)
        infoTextView = findViewById(R.id.infoTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        randomButton = findViewById(R.id.randomButton)
        anotherMovieButton = findViewById(R.id.anotherMovieButton)
        profileButton = findViewById(R.id.profileButton)
        moreInfoButton = findViewById(R.id.moreInfoButton)
        actorButton = findViewById(R.id.actorButton)

        // Восстановление состояния или загрузка первого фильма
        currentMovie = if (savedInstanceState != null) {
            val movieId = savedInstanceState.getInt(KEY_MOVIE_ID, -1)
            Log.d(TAG, "Restoring state: movieId=$movieId")

            if (movieId != -1) {
                MovieRepository.findMovieById(movieId)
            } else {
                MovieRepository.getRandomMovie()
            }
        } else {
            Log.d(TAG, "First launch: loading random movie")
            MovieRepository.getRandomMovie()
        }

        displayMovie(currentMovie)
        setupClickListeners()

        updateGreeting()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called - Activity is now visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - Activity is now interactive")
        updateGreeting()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        currentMovie?.let {
            outState.putInt(KEY_MOVIE_ID, it.id)
            Log.d(TAG, "onSaveInstanceState: saved movieId=${it.id}")
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

    private fun setupClickListeners() {
        // Кнопка "Случайный фильм"
        randomButton.setOnClickListener {
            Log.d(TAG, "Random button clicked")

            val previousMovie = currentMovie

            currentMovie = currentMovie?.let { current ->
                MovieRepository.getRandomMovieExcluding(current.id)
            } ?: MovieRepository.getRandomMovie()

            Log.d(TAG, "Changed movie: ${previousMovie?.title} -> ${currentMovie?.title}")

            displayMovie(currentMovie)
        }

        // ДОМАШНЕЕ ЗАДАНИЕ 1: Кнопка "Другой фильм"
        anotherMovieButton.setOnClickListener {
            Log.d(TAG, "Another movie button clicked")

            val previousMovie = currentMovie

            // Загружаем другой фильм (гарантированно отличный от текущего)
            currentMovie = currentMovie?.let { current ->
                MovieRepository.getRandomMovieExcluding(current.id)
            } ?: MovieRepository.getRandomMovie()

            // Логируем смену фильма
            Log.d(TAG, "Movie changed from '${previousMovie?.title}' to '${currentMovie?.title}'")

            // Обновляем UI
            displayMovie(currentMovie)

            // Toast для демонстрации
            Toast.makeText(
                this,
                "Загружен фильм: ${currentMovie?.title}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Кнопка "Подробнее"
        moreInfoButton.setOnClickListener {
            Log.d(TAG, "More info button clicked")

            currentMovie?.let { movie ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.kinopoiskUrl))

                try {
                    startActivity(intent)
                    Log.d(TAG, "Opening URL: ${movie.kinopoiskUrl}")
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this,
                        "Нет приложения для открытия ссылки",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.w(TAG, "No app found to handle URL", e)
                }
            }
        }

        profileButton.setOnClickListener {
            Log.d(TAG, "Profile button clicked")
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        actorButton.setOnClickListener {
            Log.d(TAG, "Actor button clicked")
            val intent = Intent(this, ActorDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateGreeting() {
        val profile = ProfileRepository.currentProfile
        if (profile != null) {
            greetingTextView.text = getString(R.string.greeting_format, profile.name)
            greetingTextView.visibility = View.VISIBLE
            Log.d(TAG, "Showing greeting for ${profile.name}")
        } else {
            greetingTextView.visibility = View.GONE
            Log.d(TAG, "No profile to greet")
        }
    }
}
