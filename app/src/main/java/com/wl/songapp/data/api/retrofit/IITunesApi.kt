package com.wl.songapp.data.api.retrofit

import com.wl.songapp.data.api.ITunesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IITunesApi {
    companion object {
        const val SEARCH_API_ENDPOINT = "search"
    }

    @GET(SEARCH_API_ENDPOINT) // adding interceptor in OKHttp client that add query only by artist name
    fun getITunesResponseForArtistName(@Query("term") name: String): Single<ITunesResponse>
}