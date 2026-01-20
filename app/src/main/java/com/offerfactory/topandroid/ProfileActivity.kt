package com.offerfactory.topandroid

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.offerfactory.topandroid.BuildConfig

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
    }

    private lateinit var viewModel: ProfileViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        setContentView(R.layout.activity_profile)

        // Инициализация UI элементов
        initViews()

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        // Настройка Spinner с жанрами
        setupGenreSpinner()

        setupObservers()

        // Настройка TextWatcher для валидации
        setupTextWatchers()

        renderInitialForm()

        // Настройка обработчиков кликов
        setupClickListeners()

        // Установка версии приложения
        versionTextView.text = getString(R.string.version_format, BuildConfig.VERSION_NAME)
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
                val genre = if (position == 0) null else parent?.getItemAtPosition(position)?.toString()
                viewModel.onGenreSelected(position, genre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.onGenreSelected(0, null)
            }
        }
    }

    private fun setupObservers() {
        viewModel.greetingName.observe(this) { name ->
            renderGreeting(name)
        }

        viewModel.isNameValid.observe(this) { isValid ->
            val name = nameEditText.text.toString()
            nameEditText.error = if (!isValid && name.isNotEmpty()) {
                getString(R.string.name_error)
            } else {
                null
            }
        }

        viewModel.isAgeValid.observe(this) { isValid ->
            val ageText = ageEditText.text.toString()
            ageEditText.error = if (!isValid && ageText.isNotEmpty()) {
                getString(R.string.age_error)
            } else {
                null
            }
        }

        viewModel.isGenreValid.observe(this) { isValid ->
            genreErrorTextView.visibility = if (isValid) View.GONE else View.VISIBLE
        }

        viewModel.isSaveEnabled.observe(this) { enabled ->
            saveButton.isEnabled = enabled
        }
    }

    private fun setupTextWatchers() {
        // TextWatcher для имени
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onNameChanged(s.toString())
            }
        })

        // TextWatcher для возраста
        ageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onAgeChanged(s.toString())
            }
        })

        aboutEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.onAboutChanged(s.toString())
            }
        })
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            Log.d(TAG, "Save button clicked")

            val profile = viewModel.saveProfile()
            if (profile != null) {
                Log.d(TAG, "Profile saved: $profile")
                Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        cancelButton.setOnClickListener {
            Log.d(TAG, "Cancel button clicked")
            finish()
        }
    }

    private fun renderGreeting(name: String?) {
        if (name != null) {
            greetingTextView.text = getString(R.string.greeting_format, name)
            greetingTextView.visibility = View.VISIBLE
        } else {
            greetingTextView.visibility = View.GONE
        }
    }

    private fun renderInitialForm() {
        nameEditText.setText(viewModel.name.value.orEmpty())
        ageEditText.setText(viewModel.ageText.value.orEmpty())
        aboutEditText.setText(viewModel.about.value.orEmpty())

        val savedGenrePosition = viewModel.genrePosition.value ?: 0
        if (savedGenrePosition != 0) {
            genreSpinner.setSelection(savedGenrePosition)
            return
        }

        val savedGenre = viewModel.genre.value
        if (savedGenre != null) {
            val genres = resources.getStringArray(R.array.genres)
            val genrePosition = genres.indexOf(savedGenre)
            if (genrePosition >= 0) {
                genreSpinner.setSelection(genrePosition)
            }
        }
    }
}
