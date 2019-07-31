package com.wl.songapp.data.api

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.JsonClass

@Parcelize
@JsonClass(generateAdapter = true)
data class ITunesResponse(
    @Json(name = "resultCount") val resultCount: Int = 0,
    @Json(name = "results") val results: List<Result>
) : Parcelable

@Parcelize
data class Result( //there are many more fields, but these will suffice
    @Json(name= "trackName ") val trackName: String? = "",
    @Json(name= "artistName ") val artistName: String? = "",
    @Json(name= "releaseDate ") val releaseDate: String? = ""
) : Parcelable