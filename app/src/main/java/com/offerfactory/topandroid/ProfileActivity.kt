package com.offerfactory.topandroid

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.offerfactory.topandroid.BuildConfig

class ProfileActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ProfileActivity"
    }

    private lateinit var viewModel: ProfileViewModel

    private lateinit var greetingTextView: TextView
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genreSpinner: Spinner
    private lateinit var aboutEditText: EditText
    private lateinit var versionTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        initViews()

        setupGenreSpinner()

        setupObservers()

        setupEventListeners()

        versionTextView.text = getString(R.string.version_format, BuildConfig.VERSION_NAME)
    }
    private fun initViews() {
        greetingTextView = findViewById(R.id.greetingTextView)
        nameEditText = findViewById(R.id.nameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genreSpinner = findViewById(R.id.genreSpinner)
        aboutEditText = findViewById(R.id.aboutEditText)
        versionTextView = findViewById(R.id.versionTextView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
    }
    private fun setupGenreSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.genres,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.name.observe(this) { name ->
            if (nameEditText.text.toString() != name) {
                nameEditText.setText(name)
            }
        }

        viewModel.age.observe(this) { age ->
            if (ageEditText.text.toString() != age) {
                ageEditText.setText(age)
            }
        }

        viewModel.genrePosition.observe(this) { position ->
            if (genreSpinner.selectedItemPosition != position) {
                genreSpinner.setSelection(position)
            }
        }

        viewModel.about.observe(this) { about ->
            if (aboutEditText.text.toString() != about) {
                aboutEditText.setText(about)
            }
        }

        viewModel.saveEnabled.observe(this) { enabled ->
            saveButton.isEnabled = enabled
            Log.d(TAG, "Save button enabled: $enabled")
        }
    }

    private fun setupEventListeners() {
        nameEditText.setOnKeyListener { _, _, _ ->
            viewModel.onNameChanged(nameEditText.text.toString())
            false
        }
        ageEditText.setOnKeyListener { _, _, _ ->
            viewModel.onAgeChanged(ageEditText.text.toString())
            false
        }

        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                viewModel.onGenreSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.onGenreSelected(0)
            }
        }

        aboutEditText.setOnKeyListener { _, _, _ ->
            viewModel.onAboutChanged(aboutEditText.text.toString())
            false
        }

        saveButton.setOnClickListener {
            Log.d(TAG, "Save button clicked")
            viewModel.saveProfile()

            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show()

            finish()
        }

        cancelButton.setOnClickListener {
            Log.d(TAG, "Cancel button clicked")
            finish()
        }
    }
}