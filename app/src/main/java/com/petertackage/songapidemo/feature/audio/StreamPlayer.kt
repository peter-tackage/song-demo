package com.petertackage.songapidemo.feature.audio

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_MUSIC
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.MediaPlayer

class StreamPlayer {

    private var mediaPlayer : MediaPlayer? = null

    fun play(url: String) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA).build()
            )
            setDataSource(url)
            setOnPreparedListener { start() }
            prepareAsync()
        }

    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun release() {
        mediaPlayer?.release()
    }
}