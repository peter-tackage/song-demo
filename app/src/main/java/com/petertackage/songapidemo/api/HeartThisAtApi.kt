package com.petertackage.songapidemo.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeartThisAtApi {

    @GET("/feed/?type=popular")
    suspend fun popularFeed(
        @Query("page") page: Int,
        @Query("count") count: Int
    ): List<FeedItemJson>

    @GET("/{permalink}/")
    suspend fun artist(@Path("permalink") permalink: String): ArtistJson

    @GET("/{permalink}/?type=tracks")
    suspend fun tracks(
        @Path("permalink") permalink: String,
        @Query("page") page: Int,
        @Query("count") count: Int
    ): List<TrackJson>

}

data class FeedItemJson(
    val id: String,
    @Json(name = "user_id") val userId: String,
    val user: UserJson
)

data class UserJson(
    val id: String,
    val permalink: String
)

data class ArtistJson(
    val id: String,
    val permalink: String,
    val username: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "background_url") val backgroundUrl: String,
    val description: String,
    @Json(name = "track_count") val trackCount: Int,
    @Json(name = "followers_count") val followersCount: Long
)

data class TrackJson(
    val id: String,
    val title: String,
    val genre: String,
    @Json(name = "artwork_url") val artworkUrl: String,
    val duration: Long,
    @Json(name = "waveform_url") val waveformUrl: String,
    @Json(name = "stream_url") val streamUrl: String
)
