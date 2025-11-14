package com.offerfactory.topandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

/**
 * Activity для отображения детальной информации о фильме.
 * 
 * В этом классе демонстрируются ключевые концепции Android:
 * 1. Жизненный цикл Activity (onCreate, onStart, onResume, onPause, onStop, onDestroy)
 * 2. Работа с UI-элементами через findViewById
 * 3. Обработка кликов на кнопки
 * 4. Неявные Intent для открытия браузера
 * 5. Сохранение состояния при повороте экрана (onSaveInstanceState)
 * 
 * ВАЖНО: Activity — один из четырёх основных компонентов Android.
 * Каждая Activity должна быть объявлена в AndroidManifest.xml.
 */
class MovieDetailActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MovieDetailActivity"
        
        private const val KEY_MOVIE_ID = "movie_id"
    }

    private var currentMovie: Movie? = null

    private val viewModel: MovieViewModel by viewModels()
    
    // UI элементы (findViewById возвращает View, приводим к конкретному типу)
    private lateinit var titleTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var randomButton: Button
    private lateinit var moreInfoButton: Button

    
    /**
     * onCreate() — ПЕРВЫЙ метод жизненного цикла при создании Activity.
     * 
     * Вызывается ОДИН РАЗ при создании Activity.
     * Здесь нужно:
     * 1. Вызвать super.onCreate() — ОБЯЗАТЕЛЬНО и ПЕРВЫМ!
     * 2. Установить layout через setContentView()
     * 3. Инициализировать UI элементы
     * 4. Восстановить состояние из savedInstanceState (при повороте экрана)
     * 5. Настроить слушателей событий
     * 
     * savedInstanceState содержит данные, сохранённые в onSaveInstanceState()
     * при пересоздании Activity (например, при повороте экрана).
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate called, savedInstanceState: $savedInstanceState")
        
        // Устанавливаем layout для этой Activity
        // R.layout.activity_movie_detail ссылается на res/layout/activity_movie_detail.xml
        setContentView(R.layout.activity_movie_detail)
        
        // Инициализация UI элементов
        // findViewById находит View по ID из XML
        titleTextView = findViewById(R.id.titleTextView)
        infoTextView = findViewById(R.id.infoTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        moreInfoButton = findViewById(R.id.moreInfoButton)
        randomButton = findViewById(R.id.randomMovieButton)
        currentMovie = MovieRepository.getRandomMovie()
        Log.d(TAG, "First launch: loading random movie")

        if (savedInstanceState != null) {
            val savedMovieId = savedInstanceState.getInt(KEY_MOVIE_ID, -1)
            if (savedMovieId != -1) {
                currentMovie = MovieRepository.movies.find { it.id == savedMovieId }
                Log.d(TAG, "Restored movie from saved state: ${currentMovie?.title}")
            }
        }

        if (currentMovie == null) {
            currentMovie = MovieRepository.getRandomMovie()
            Log.d(TAG, "First launch: loading random movie")
        }

        // Отображаем информацию о фильме
        displayMovie(currentMovie)
        
        // Настройка обработчиков кликов
        setupClickListeners()
        setupRandomButtonListener()

    }
    
    /**
     * onStart() — вызывается когда Activity становится ВИДИМОЙ для пользователя.
     * 
     * После onCreate() или после onRestart() (когда возвращаемся из остановленного состояния).
     * Парный метод: onStop()
     * 
     * Здесь можно:
     * - Регистрировать слушателей (BroadcastReceiver)
     * - Начать анимации UI
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called - Activity is now visible")
    }
    
    /**
     * onResume() — вызывается когда Activity получает ФОКУС и готова к взаимодействию.
     * 
     * Activity находится на переднем плане и пользователь может с ней взаимодействовать.
     * Парный метод: onPause()
     * 
     * Здесь можно:
     * - Запустить воспроизведение видео
     * - Возобновить игру
     * - Начать обновления UI
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called - Activity is now interactive")
    }
    
    /**
     * onPause() — вызывается когда Activity ТЕРЯЕТ ФОКУС.
     * 
     * Это может произойти когда:
     * - Открывается другая Activity
     * - Появляется диалог
     * - Система показывает многозадачность
     * 
     * ВАЖНО: Этот метод должен выполняться БЫСТРО!
     * Следующая Activity не запустится, пока onPause() не завершится.
     * 
     * Здесь нужно:
     * - Остановить анимации
     * - Паузить видео
     * - Сохранить несохранённые данные
     * 
     * НЕ делайте здесь:
     * - Длительные операции
     * - Сетевые запросы
     * - Операции с БД (лучше в onStop)
     */
    override fun onPause() {
        Log.d(TAG, "onPause called - Activity is losing focus")
        
        // ВАЖНО: super.onPause() вызываем ПОСЛЕ нашего кода
        super.onPause()
    }
    
    /**
     * onStop() — вызывается когда Activity больше НЕ ВИДНА пользователю.
     * 
     * Это происходит когда:
     * - Пользователь переходит в другую Activity
     * - Нажимает Home
     * - Открывает список задач
     * 
     * Здесь можно:
     * - Остановить дорогие операции (обновления UI, анимации)
     * - Сохранить данные в БД
     * - Освободить ресурсы, которые не нужны когда Activity не видна
     */
    override fun onStop() {
        Log.d(TAG, "onStop called - Activity is no longer visible")
        super.onStop()
    }
    
    /**
     * onRestart() — вызывается когда Activity возвращается из STOPPED состояния.
     * 
     * Вызывается ПЕРЕД onStart() когда пользователь возвращается к остановленной Activity.
     * Например: вернулся из другого приложения, нажал Back из следующей Activity.
     * 
     * Полезно для подготовки к повторному показу Activity.
     */
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart called - Activity is being restarted")
    }
    
    /**
     * onDestroy() — ПОСЛЕДНИЙ метод жизненного цикла перед уничтожением Activity.
     * 
     * Вызывается когда:
     * - Пользователь закрывает Activity (нажал Back)
     * - finish() вызвана в коде
     * - Система уничтожает Activity для освобождения памяти
     * - Изменяется конфигурация (поворот экрана) — Activity пересоздаётся
     * 
     * Здесь нужно:
     * - Освободить ВСЕ ресурсы
     * - Отменить фоновые задачи
     * - Закрыть соединения
     * - Отписаться от слушателей
     */
    override fun onDestroy() {
        Log.d(TAG, "onDestroy called - Activity is being destroyed")
        super.onDestroy()
    }

    
    /**
     * Отображение информации о фильме в UI.
     * 
     * Обновляет все TextView текущими данными фильма.
     */
    private fun displayMovie(movie: Movie?) {
        movie?.let {
            Log.d(TAG, "Displaying movie: ${it.title}")
            
            // Обновление TextView
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
    
    /**
     * Настройка обработчиков кликов для кнопок.
     * 
     * setOnClickListener принимает лямбду, которая выполняется при клике.
     */
    private fun setupClickListeners() {
        // Кнопка "Подробнее" — открывает браузер с Kinopoisk
        moreInfoButton.setOnClickListener {
            Log.d(TAG, "More info button clicked")
            
            currentMovie?.let { movie ->
                val intent = Intent(Intent.ACTION_VIEW, movie.kinopoiskUrl.toUri())
                val profileButton = findViewById<Button>(R.id.profileButton)
                profileButton.setOnClickListener {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                
                // ВАЖНО: Проверяем, что есть приложение для обработки Intent
                // Без этой проверки приложение может упасть, если нет браузера
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
            Log.d(TAG, "Смена фильма. $previousMovie $newMovie")

            Toast.makeText(this, "Фильм изменен!", Toast.LENGTH_SHORT).show()
        }
    }

}

/**
 * КРАТКАЯ СПРАВКА ПО ЖИЗНЕННОМУ ЦИКЛУ:
 * 
 * Запуск приложения:
 * onCreate() → onStart() → onResume() → [Activity активна]
 * 
 * Переход в другое приложение (Home):
 * onPause() → onStop() → [Activity в фоне]
 * 
 * Возврат в приложение:
 * onRestart() → onStart() → onResume() → [Activity активна]
 * 
 * Поворот экрана:
 * onPause() → onStop() → onSaveInstanceState() → onDestroy() →
 * → onCreate(savedInstanceState) → onStart() → onResume()
 * 
 * Закрытие Activity (Back):
 * onPause() → onStop() → onDestroy() → [Activity уничтожена]
 * 
 * ВАЖНО: Всегда вызывайте super.onXxx()!
 * - В onCreate, onStart, onResume — ПЕРВЫМ
 * - В onPause, onStop, onDestroy — ПОСЛЕДНИМ
 */
