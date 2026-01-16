package com.offerfactory.topandroid

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class ActorDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ActorDetailActivity"
        const val EXTRA_ACTOR_ID = "actor_id"
    }

    private lateinit var viewModel: ActorDetailViewModel

    // UI элементы
    private lateinit var nameText: TextView
    private lateinit var subtitleText: TextView
    private lateinit var bioText: TextView
    private lateinit var posterView: ImageView
    private lateinit var knownForText1: TextView
    private lateinit var knownForText2: TextView
    private lateinit var knownForText3: TextView
    private lateinit var knownForText4: TextView
    private lateinit var knownForText5: TextView
    private lateinit var openWikiButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_actor_detail)

        viewModel = ViewModelProvider(this)[ActorDetailViewModel::class.java]

        initViews()

        setupObservers()

        setupClickListeners()

        loadActorFromIntent()
    }

    private fun initViews() {
        nameText = findViewById(R.id.actorNameText)
        subtitleText = findViewById(R.id.actorSubtitleText)
        bioText = findViewById(R.id.actorBioText)
        posterView = findViewById(R.id.actorPoster)
        knownForText1 = findViewById(R.id.knownFor1)
        knownForText2 = findViewById(R.id.knownFor2)
        knownForText3 = findViewById(R.id.knownFor3)
        knownForText4 = findViewById(R.id.knownFor4)
        knownForText5 = findViewById(R.id.knownFor5)
        openWikiButton = findViewById(R.id.openWikiButton)
    }

    private fun setupObservers() {
        viewModel.actor.observe(this) { actor ->
            actor?.let {
                displayActor(it)
            } ?: run {
                Log.w(TAG, "Received null actor")

            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                // Можно показать ProgressBar
                Log.d(TAG, "Loading in progress...")
            } else {
                // Скрыть ProgressBar
                Log.d(TAG, "Loading completed")
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                Log.w(TAG, "Error: $it")
            }
        }
    }

    private fun loadActorFromIntent() {
        val actorId = intent.getIntExtra(EXTRA_ACTOR_ID, -1)

        if (actorId != -1) {
            Log.d(TAG, "Loading actor with ID: $actorId")
            viewModel.loadActor(actorId)
        } else {
            Log.d(TAG, "No actor ID provided, loading default actor")
            viewModel.loadDefaultActor()
        }
    }

    private fun displayActor(actor: Actor) {
        Log.d(TAG, "Displaying actor: ${actor.name}")

        nameText.text = actor.name
        subtitleText.text = getString(R.string.actor_subtitle_format, actor.birthYear, actor.country)
        bioText.text = actor.bio

        Glide.with(this)
            .load(actor.avatarUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.darker_gray)
            .into(posterView)

        displayKnownFor(actor.knownFor)

        openWikiButton.isEnabled = actor.wikiUrl.isNotBlank()
    }

    private fun displayKnownFor(knownFor: List<String>) {
        val items = knownFor.take(5)
        val textViews = listOf(
            knownForText1, knownForText2, knownForText3,
            knownForText4, knownForText5
        )

        textViews.forEachIndexed { index, textView ->
            if (index < items.size && items[index].isNotBlank()) {
                textView.text = items[index]
                textView.visibility = View.VISIBLE
            } else {
                textView.visibility = View.GONE
            }
        }
    }


    private fun setupClickListeners() {
        openWikiButton.setOnClickListener {
            val wikiUrl = viewModel.getWikiUrl()

            if (!wikiUrl.isNullOrBlank()) {
                Log.d(TAG, "Opening wiki URL: $wikiUrl")
                openUrl(wikiUrl)
            } else {
                Toast.makeText(this, "Ссылка на Wiki недоступна", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Wiki URL is empty or null")
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.no_app_for_link, Toast.LENGTH_SHORT).show()
            Log.w(TAG, "No app found to handle URL: $url", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")

    }

    override fun onPause() {
        Log.d(TAG, "onPause called")
        super.onPause()
    }
}