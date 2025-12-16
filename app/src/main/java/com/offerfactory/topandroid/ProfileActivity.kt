package com.offerfactory.topandroid

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.offerfactory.topandroid.BuildConfig

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"

        // Ключи для сохранения состояния в Bundle
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_GENRE_POSITION = "genre_position"
        private const val KEY_ABOUT = "about"

    }

    // UI элементы
    private lateinit var greetingTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genreLabel: TextView
    private lateinit var genreSpinner: Spinner
    private lateinit var genreErrorTextView: TextView
    private lateinit var aboutEditText: EditText
    private lateinit var versionTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    // Флаги валидации
    private var isNameValid = false
    private var isAgeValid = false
    private var isGenreValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        setContentView(R.layout.activity_profile)

        // Инициализация UI элементов
        initViews()

        // Настройка Spinner с жанрами
        setupGenreSpinner()

        // Настройка TextWatcher для валидации
        setupTextWatchers()

        // Настройка обработчиков кликов
        setupClickListeners()

        // Установка версии приложения
        versionTextView.text = getString(R.string.version_format, BuildConfig.VERSION_NAME)

        // Восстановление состояния или загрузка сохранённого профиля
        if (savedInstanceState != null) {
            // Восстанавливаем несохранённые данные формы после поворота
            restoreFormState(savedInstanceState)
        } else {
            // Загружаем сохранённый профиль из репозитория
            loadSavedProfile()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        Log.d(TAG, "onPause called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")

        // Сохраняем текущие значения полей формы
        outState.putString(KEY_NAME, nameEditText.text.toString())
        outState.putString(KEY_AGE, ageEditText.text.toString())
        outState.putInt(KEY_GENRE_POSITION, genreSpinner.selectedItemPosition)
        outState.putString(KEY_ABOUT, aboutEditText.text.toString())
    }

    private fun initViews() {
        greetingTextView = findViewById(R.id.greetingTextView)
        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genreLabel = findViewById(R.id.genreLabel)
        genreSpinner = findViewById(R.id.genreSpinner)
        genreErrorTextView = findViewById(R.id.genreErrorTextView)
        aboutEditText = findViewById(R.id.aboutEditText)
        versionTextView = findViewById(R.id.versionTextView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
    }

    private fun setupGenreSpinner() {
        // Создаём адаптер из строкового массива
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter

        // Слушатель выбора элемента
        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Позиция 0 — это "Выберите жанр" (невалидный выбор)
                isGenreValid = position != 0

                // Показываем/скрываем ошибку
                genreErrorTextView.visibility = if (isGenreValid) View.GONE else View.VISIBLE

                updateSaveButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                isGenreValid = false
                updateSaveButtonState()
            }
        }
    }

    private fun setupTextWatchers() {
        // TextWatcher для имени
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()
                isNameValid = validateName(name)

                // Показываем ошибку если невалидно
                nameEditText.error = if (!isNameValid && name.isNotEmpty()) {
                    getString(R.string.name_error)
                } else {
                    null
                }

                updateSaveButtonState()
            }
        })

        // TextWatcher для возраста
        ageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val ageText = s.toString()
                isAgeValid = validateAge(ageText)

                // Показываем ошибку если невалидно
                ageEditText.error = if (!isAgeValid && ageText.isNotEmpty()) {
                    getString(R.string.age_error)
                } else {
                    null
                }

                updateSaveButtonState()
            }
        })
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            Log.d(TAG, "Save button clicked")

            // Собираем данные из формы
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toInt()
            val genre = genreSpinner.selectedItem.toString()
            val about = aboutEditText.text.toString()

            // Создаём и сохраняем профиль
            val profile = UserProfile(name, age, genre, about)
            ProfileRepository.currentProfile = profile

            Log.d(TAG, "Profile saved: $profile")

            // Показываем Toast
            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()

            // Закрываем Activity
            finish()
        }

        cancelButton.setOnClickListener {
            Log.d(TAG, "Cancel button clicked")
            finish()
        }
    }

    private fun validateName(name: String): Boolean {
        return name.length in 2..40
    }

    private fun validateAge(ageText: String): Boolean {
        val age = ageText.toIntOrNull() ?: return false
        return age in 10..120
    }

    private fun updateSaveButtonState() {
        // Кнопка активна только когда все поля валидны
        saveButton.isEnabled = isNameValid && isAgeValid && isGenreValid

        Log.d(TAG, "Save button state: enabled=${saveButton.isEnabled} " +
                "(name=$isNameValid, age=$isAgeValid, genre=$isGenreValid)")
    }

    private fun restoreFormState(savedInstanceState: Bundle) {
        Log.d(TAG, "Restoring form state from Bundle")

        // Восстанавливаем значения полей
        val name = savedInstanceState.getString(KEY_NAME, "")
        val age = savedInstanceState.getString(KEY_AGE, "")
        val genrePosition = savedInstanceState.getInt(KEY_GENRE_POSITION, 0)
        val about = savedInstanceState.getString(KEY_ABOUT, "")

        nameEditText.setText(name)
        ageEditText.setText(age)
        genreSpinner.setSelection(genrePosition)
        aboutEditText.setText(about)
    }

    private fun loadSavedProfile() {
        // Загружаем сохранённый профиль если есть
        ProfileRepository.currentProfile?.let { profile ->
            Log.d(TAG, "Loading saved profile: ${profile.name}")

            nameEditText.setText(profile.name)
            ageEditText.setText(profile.age.toString())
            aboutEditText.setText(profile.about)

            // Находим позицию жанра в массиве
            val genres = resources.getStringArray(R.array.genres)
            val genrePosition = genres.indexOf(profile.genre)
            if (genrePosition >= 0) {
                genreSpinner.setSelection(genrePosition)
            }

            // Показываем приветствие
            greetingTextView.text = getString(R.string.greeting_format, profile.name)
            greetingTextView.visibility = View.VISIBLE
        }
    }
}
