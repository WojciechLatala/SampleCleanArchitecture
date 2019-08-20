package com.wl.songapp.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SongData(
    @Json(name = "Song Clean") val trackName: String,
    @Json(name = "ARTIST CLEAN") val artistName: String,
    @Json(name = "Release Year") val releaseYear: String?
)