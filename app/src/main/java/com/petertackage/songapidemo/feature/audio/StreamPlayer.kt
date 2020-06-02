package com.petertackage.songapidemo.feature.audio

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petertackage.songapidemo.service.Track

// FIXME This doesn't handle error recovery or wake locks - it seemed beyond the scope of the task.
object StreamPlayer {

    private val _state = MutableLiveData(StreamState(null, false))
    val state: LiveData<StreamState> get() = _state

    private var mediaPlayer: MediaPlayer? = null

    fun play(track: Track) {
        performStop()
        performPlay(track)
    }

    fun stop() {
        performStop()
    }

    private fun performPlay(track: Track) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA).build()
            )
            setDataSource(track.streamUrl)
            setOnPreparedListener { start(); emitPlaying(track) }
            prepareAsync()
        }
    }

    private fun performStop() {
        mediaPlayer?.also { it.stop() }
            .also { emitIdle() }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        emitIdle()
    }

    private fun emitPlaying(track: Track) {
        _state.value = StreamState(track, true)
    }

    private fun emitStopped() {
        _state.value = _state.value?.copy(isPlaying = false) ?: StreamState(null, false)
    }

    private fun emitIdle() {
        _state.value = StreamState(null, false)
    }

}

data class StreamState(
    val track: Track?,
    val isPlaying: Boolean
)