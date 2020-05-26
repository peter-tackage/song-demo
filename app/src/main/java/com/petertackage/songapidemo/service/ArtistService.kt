package com.petertackage.songapidemo.service

import com.petertackage.songapidemo.api.ArtistJson
import com.petertackage.songapidemo.api.FeedTypeJson
import com.petertackage.songapidemo.api.HeartThisAtApi

class ArtistService(private val api: HeartThisAtApi) {

    suspend fun topArtists(): List<Artist> {
        // Query the API for the feed,
        // Take the userId
        // Unique
        // Query each artist based upon username
        // Take results and map to Artist
        return api.feed(FeedTypeJson.POPULAR, page = 1, count = 20)
            .map { it.user.permalink }
            .distinct() // This will probably mess with the API paging, if duplicates
            .map { api.artist(it) }
            .map { toDomain(it) }
    }

    // TODO This will need to be expanded for track information.
    suspend fun artist(name: String): Artist {
        return toDomain(api.artist(name))
    }

}

data class Artist(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val backgroundUrl: String,
    val description: String,
    val trackCount: Int,
    val followersCount: Long
)

private fun toDomain(artistJson: ArtistJson) =
    Artist(
        id = artistJson.id,
        name = artistJson.username,
        avatarUrl = artistJson.avatarUrl,
        backgroundUrl = artistJson.backgroundUrl,
        description = artistJson.description,
        trackCount = artistJson.trackCount,
        followersCount = artistJson.followersCount
    )