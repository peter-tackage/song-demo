package com.petertackage.songapidemo.feature.audio

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// FIXME This doesn't handle error recovery or wake locks - it seemed beyond the scope of the task.
class StreamPlayer {

    private val _state = MutableLiveData<StreamState>()
    val state: LiveData<StreamState> get() = _state

    private var mediaPlayer: MediaPlayer? = null

    fun play(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA).build()
            )
            setDataSource(url)
            setOnPreparedListener { start(); emitPlaying(url) }
            prepareAsync()
        }

    }

    fun stop() {
        mediaPlayer?.also { stop() }
            .also { emitIdle() }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        emitIdle()
    }

    private fun emitPlaying(url: String) {
        _state.value = StreamState(url)
    }

    private fun emitIdle() {
        _state.value = StreamState(null)
    }

}

data class StreamState(
    val uri: String?
)