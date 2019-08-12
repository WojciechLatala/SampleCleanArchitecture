package com.wl.songapp.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ITunesResponse(
    @Json(name = "resultCount") val resultCount: Int = 0,
    @Json(name = "results") val results: List<Result>
)

data class Result( //there are many more fields, but these will suffice
    val trackName: String,
    val artistName: String,
    val releaseDate: String? = null
)