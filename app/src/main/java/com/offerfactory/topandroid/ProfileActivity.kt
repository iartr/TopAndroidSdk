package com.offerfactory.topandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.view.View
import android.widget.*

/**
 * Activity для редактирования профиля пользователя.
 */
class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_GENRE = "genre"
        private const val KEY_BIO = "bio"
        const val EXTRA_SAVED_NAME = "saved_name"
    }


    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genreSpinner: Spinner
    private lateinit var bioEditText: EditText
    private lateinit var versionTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var isNameValid = false
    private var isAgeValid = false
    private var isGenreValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        Log.d(TAG, "onCreate called")

        ProfileRepository.init(this)

        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genreSpinner = findViewById(R.id.genreSpinner)
        bioEditText = findViewById(R.id.bioEditText)
        versionTextView = findViewById(R.id.versionTextView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        loadProfileData()
        setupGenreSpinner()
        setupTextWatchers()
        setupClickListeners()
        setVersionInfo()
    }

    private fun loadProfileData() {
        val profile = ProfileRepository.getProfile() ?: Profile(
            name = "Гость",
            age = 0,
            genre = "Не указан",
            bio = ""
        )

        nameEditText.setText(profile.name)
        ageEditText.setText(profile.age.toString())
        bioEditText.setText(profile.bio)



        validateName(profile.name)
        validateAge(profile.age.toString())
        updateSaveButtonState()
    }

    private fun saveProfile() {
        val name = nameEditText.text.toString()
        val age = ageEditText.text.toString().toIntOrNull() ?: 0
        val genre = genreSpinner.selectedItem as String
        val bio = bioEditText.text.toString()

        val profile = Profile(name, age, genre, bio)
        ProfileRepository.saveProfile(profile)

        Toast.makeText(this, "Профиль сохранён", Toast.LENGTH_SHORT).show()

        val resultIntent = Intent().apply {
            putExtra(EXTRA_SAVED_NAME, name)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun validateName(name: String): Boolean {
        val isValid = name.length in 2..40
        if (!isValid && name.isNotEmpty()) {
            nameEditText.error = "Имя должно быть от 2 до 40 символов"
        } else {
            nameEditText.error = null
        }
        return isValid
    }

    private fun validateAge(ageText: String): Boolean {
        if (ageText.isEmpty()) return false

        val age = ageText.toIntOrNull()
        val isValid = age != null && age in 10..120

        if (!isValid && ageText.isNotEmpty()) {
            ageEditText.error = "Возраст должен быть от 10 до 120 лет"
        } else {
            ageEditText.error = null
        }
        return isValid
    }

    private fun setupGenreSpinner() {
        val genres = arrayOf(
            "Выберите жанр",
            "Драма",
            "Комедия",
            "Боевик",
            "Триллер",
            "Фантастика",
            "Мелодрама",
            "Фэнтези",
            "Ужасы",
            "Детектив"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter

        val savedProfile = ProfileRepository.getProfile()
        if (savedProfile != null) {
            val position = genres.indexOf(savedProfile.genre)
            if (position != -1) {
                genreSpinner.setSelection(position)
                isGenreValid = true
            }
        }

        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                isGenreValid = position != 0
                updateSaveButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupTextWatchers() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                isNameValid = validateName(s.toString())
                updateSaveButtonState()
            }
        })

        ageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                isAgeValid = validateAge(s.toString())
                updateSaveButtonState()
            }
        })
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            if (isNameValid && isAgeValid && isGenreValid) {
                saveProfile()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun updateSaveButtonState() {
        saveButton.isEnabled = isNameValid && isAgeValid && isGenreValid
    }

    private fun setVersionInfo() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionTextView.text = "Версия приложения: ${packageInfo.versionName}"
        } catch (e: Exception) {
            versionTextView.text = "Версия приложения: 1.0"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")

        outState.putString(KEY_NAME, nameEditText.text.toString())
        outState.putString(KEY_AGE, ageEditText.text.toString())
        outState.putString(KEY_BIO, bioEditText.text.toString())
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
}