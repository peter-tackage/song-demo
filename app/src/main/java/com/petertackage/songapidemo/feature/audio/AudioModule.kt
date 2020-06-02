package com.petertackage.songapidemo.feature.audio

import timber.log.Timber

fun provideStreamPlayer(): StreamPlayer {
    return StreamPlayer.also { Timber.d("Returning Stream Player instance: ${it.hashCode()}") }
}
