package com.petertackage.songapidemo.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

fun provideHeartThisAtApi(): HeartThisAtApi {
    return Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
        .client(createOkHttpClient())
        .baseUrl(" https://api-v2.hearthis.at")
        .build()
        .create(HeartThisAtApi::class.java)
}

private fun provideMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        //  .add(FeedTypeJson::class.java, EnumJsonAdapter.create(FeedTypeJson::class.java))
        .build()
}

private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(provideLoggingInterceptor())
        .build()
}

fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor((object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("OkHttp").e(message)
        }
    })).setLevel(HttpLoggingInterceptor.Level.BODY)
}