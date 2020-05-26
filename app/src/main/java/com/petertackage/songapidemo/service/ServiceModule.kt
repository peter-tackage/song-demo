package com.petertackage.songapidemo.service

import com.petertackage.songapidemo.api.provideHeartThisAtApi

fun provideArtistService(): ArtistService {
    return ArtistService(provideHeartThisAtApi())
}