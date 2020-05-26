package com.petertackage.songapidemo.service

import com.petertackage.songapidemo.api.ArtistJson
import com.petertackage.songapidemo.api.HeartThisAtApi
import com.petertackage.songapidemo.api.TrackJson

class ArtistService(private val api: HeartThisAtApi) {

    suspend fun topArtists(): List<Artist> {
        // Query the API for the feed,
        // Take the userId
        // Unique
        // Query each artist based upon username
        // Take results and map to Artist
        return api.popularFeed(page = 1, count = 20)
            .map { it.user.permalink }
            .distinct() // This will probably mess with the API paging, if duplicates
            .map { api.artist(it) }
            .map { it.toDomain() }
    }

    // TODO This will need to be expanded for track information.
    suspend fun artist(name: String): Artist {
        return api.artist(name).toDomain()
    }

    suspend fun tracksForArtist(artistName: String): List<Track> {
        return api.tracks(artistName, 1, 20)
            .map { it.toDomain() }
    }

}

data class Artist(
    val id: String,
    val nameId: String,
    val name: String,
    val avatarUrl: String,
    val backgroundUrl: String,
    val description: String,
    val trackCount: Int,
    val followersCount: Long
)

private fun ArtistJson.toDomain() =
    Artist(
        id = id,
        nameId = permalink,
        name = username,
        avatarUrl = avatarUrl,
        backgroundUrl = backgroundUrl,
        description = description,
        trackCount = trackCount,
        followersCount = followersCount
    )


data class Track(
    val id: String,
    val title: String,
    val genre: String,
    val artworkUrl: String,
    val duration: Long,
    val waveformUrl: String,
    val streamUrl: String
)

private fun TrackJson.toDomain() =
    Track(
        id = id,
        title = title,
        genre = genre,
        artworkUrl = artworkUrl,
        duration = duration,
        waveformUrl = waveformUrl,
        streamUrl = streamUrl
    )