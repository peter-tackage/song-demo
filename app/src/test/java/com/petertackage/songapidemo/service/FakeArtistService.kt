package com.petertackage.songapidemo.service

import kotlinx.coroutines.delay

class FakeArtistService : ArtistService {

    // https://youtrack.jetbrains.com/issue/KT-3110
    // You can't have public setters without public getters
    var delayMillis: Long = 0
    var nextError: Throwable? = null
    var nextTopArtists: List<Artist> = emptyList()
    var nextArtist: Artist? = null
    var nextArtistTracks: List<Track> = emptyList()

    override suspend fun topArtists(): List<Artist> {
        performDelay()
        throwIfError()
        return nextTopArtists
    }

    private fun throwIfError() {
        if (nextError != null) {
            throw nextError as Throwable
        }
    }

    private suspend fun performDelay() {
        if (delayMillis > 0) delay(delayMillis)
    }

    override suspend fun artist(name: String): Artist {
        performDelay()
        throwIfError()
        return nextArtist!!
    }

    override suspend fun tracksForArtist(artistName: String): List<Track> {
        performDelay()
        throwIfError()
        return nextArtistTracks
    }

}
