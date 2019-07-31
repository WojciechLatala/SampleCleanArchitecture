package com.wl.songapp.data

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Song(
    @Json(name = "ARTIST CLEAN") val artistName: String = "",
    @Json(name = "Song Clean") val songName: String = "",
    @Json(name = "Release Year") val yearPublished: String? = "") : Parcelable