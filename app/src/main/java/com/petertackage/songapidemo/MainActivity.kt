package com.petertackage.songapidemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.petertackage.songapidemo.databinding.AudioPlayerViewBinding
import com.petertackage.songapidemo.databinding.MainActivityBinding
import com.petertackage.songapidemo.service.Track
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
        with(binding.audioPlayerContent) {
            state.track?.let { track -> renderTrack(track, state.isPlaying) } ?: renderIdlePlayer()
        }
    }

    private fun AudioPlayerViewBinding.renderTrack(
        track: Track,
        isPlaying: Boolean
    ) {
        Glide.with(this@MainActivity).load(track.artworkUrl)
            .into(imageViewAudioPlayerAvatar)
        textViewAudioPlayerTrackTitle.text = track.title
        imageViewAudioPlayerControls.setImageResource(if (isPlaying) R.drawable.ic_baseline_stop_24 else R.drawable.ic_baseline_play_circle_outline_24)
    }

    private fun AudioPlayerViewBinding.renderIdlePlayer() {
        Glide.with(this@MainActivity).clear(imageViewAudioPlayerAvatar)
        imageViewAudioPlayerControls.setImageDrawable(null)
        textViewAudioPlayerTrackTitle.text = "-"
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