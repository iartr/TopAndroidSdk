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
import androidx.lifecycle.ViewModelProvider

class MovieDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MovieDetailActivity"
    }

    private lateinit var viewModel: MovieDetailViewModel

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

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

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

        setupObservers()
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called - Activity is now visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - Activity is now interactive")
        viewModel.refreshGreeting()
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

    private fun setupObservers() {
        viewModel.movie.observe(this) { movie ->
            displayMovie(movie)
        }

        viewModel.greetingName.observe(this) { name ->
            renderGreeting(name)
        }
    }

    private fun displayMovie(movie: Movie) {
        Log.d(TAG, "Displaying movie: ${movie.title}")

        titleTextView.text = movie.title
        infoTextView.text = "${movie.year} • ${movie.genre} • ${movie.duration}"
        descriptionTextView.text = movie.description
        ratingTextView.text = movie.rating.toString()
    }

    private fun setupClickListeners() {
        // Кнопка "Случайный фильм"
        randomButton.setOnClickListener {
            Log.d(TAG, "Random button clicked")
            viewModel.loadRandomMovie()
        }

        // ДОМАШНЕЕ ЗАДАНИЕ 1: Кнопка "Другой фильм"
        anotherMovieButton.setOnClickListener {
            Log.d(TAG, "Another movie button clicked")
            viewModel.loadRandomMovie()

            // Toast для демонстрации
            Toast.makeText(
                this,
                "Загружен фильм: ${viewModel.movie.value?.title}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Кнопка "Подробнее"
        moreInfoButton.setOnClickListener {
            Log.d(TAG, "More info button clicked")

            viewModel.movie.value?.let { movie ->
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

    private fun renderGreeting(name: String?) {
        if (name != null) {
            greetingTextView.text = getString(R.string.greeting_format, name)
            greetingTextView.visibility = View.VISIBLE
            Log.d(TAG, "Showing greeting for $name")
        } else {
            greetingTextView.visibility = View.GONE
            Log.d(TAG, "No profile to greet")
        }
    }
}
