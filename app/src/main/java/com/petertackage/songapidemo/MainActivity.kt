package com.petertackage.songapidemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.petertackage.songapidemo.databinding.MainActivityBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLogging()

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.state
            .observe(this,
                Observer { state -> render(state) })
    }

    private fun render(state: MainActivityState) {
        binding.audioPlayerContent.textViewAudioPlayerTrackTitle.text =
            if (state.track == null) {
                "-"
            } else {
                "${if (state.isPlaying) "Playing" else "Stopped"}: ${state.track.title}"
            }
    }

    override fun onStop() {
        super.onStop()
        // Prevent audio from running in the background.
        viewModel.releasePlayer()
    }

    private fun initLogging() {
        val loggingTree = Timber.DebugTree()
        Timber.uprootAll()
        Timber.plant(loggingTree)
    }
}