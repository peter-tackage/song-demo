package com.petertackage.songapidemo.test.data

import com.petertackage.songapidemo.service.Artist

fun artistTestFixture(index: Int): Artist {
    return Artist(
        "id$index",
        "nameId$index",
        "name$index",
        "avatarUrl$index",
        "backgroundUrl$index",
        "description$index",
        index,
        index.toLong() + 1
    )
}
