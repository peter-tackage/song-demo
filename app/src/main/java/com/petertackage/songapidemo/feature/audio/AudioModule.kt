package com.petertackage.songapidemo.feature.audio

import timber.log.Timber

private val streamPlayer: StreamPlayer by lazy { StreamPlayer() }

fun provideStreamPlayer(): StreamPlayer {
    return streamPlayer.also { Timber.d("Returning Stream Player instance: ${it.hashCode()}") }
}