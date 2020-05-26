package com.petertackage.songapidemo.api

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HeartThisAtApi {

    // https://api-v2.hearthis.at/feed/?type=popular&page=1&count=5

    @GET("/feed")
    suspend fun feed(
        @Query("type") feedType: FeedTypeJson?,
        @Query("page") page: Int,
        @Query("count") count: Int
    ): List<FeedItemJson>

    @GET("/{permalink}")
    suspend fun artist(@Path("permalink") permalink: String): ArtistJson

}

enum class FeedTypeJson {
    @Json(name = "popular")
    POPULAR,

    @Json(name = "new")
    NEW
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
