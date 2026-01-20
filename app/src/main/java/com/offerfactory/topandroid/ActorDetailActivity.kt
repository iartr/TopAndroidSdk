package com.offerfactory.topandroid

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class ActorDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ActorDetailActivity"
    }

    private lateinit var viewModel: ActorDetailViewModel

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
        setContentView(R.layout.activity_actor_detail)

        viewModel = ViewModelProvider(this)[ActorDetailViewModel::class.java]

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

        viewModel.actor.observe(this) { actor ->
            displayActor(actor)
        }
        setupClicks()
    }

    private fun displayActor(actor: Actor) {
        Log.d(TAG, "Displaying actor: ${actor.name}")
        nameText.text = actor.name
        subtitleText.text = getString(R.string.actor_subtitle_format, actor.birthYear, actor.country)
        bioText.text = actor.bio

        // Загрузим аватар актёра через Glide
        Glide.with(this)
            .load(actor.avatarUrl)
            .placeholder(android.R.color.darker_gray)
            .into(posterView)

        // Покажем до 5 элементов "Известен по" без RecyclerView
        val items = actor.knownFor.take(5)
        val tvs = listOf(knownForText1, knownForText2, knownForText3, knownForText4, knownForText5)
        tvs.forEachIndexed { index, textView ->
            if (index < items.size) {
                textView.text = items[index]
                textView.visibility = android.view.View.VISIBLE
            } else {
                textView.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupClicks() {
        openWikiButton.setOnClickListener {
            viewModel.actor.value?.let { a ->
                Log.d(TAG, "Open wiki clicked: ${a.wikiUrl}")
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(a.wikiUrl)))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, R.string.no_app_for_link, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
