package com.wl.songapp.data.api

import com.wl.songapp.data.entity.ITunesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IRemoteSongApi {
    companion object {
        const val SEARCH_API_ENDPOINT = "search?entity=song&attribute=artistTerm"
    }

    @GET(SEARCH_API_ENDPOINT)
    fun getSongsForArtistName(@Query("term") name: String): Single<ITunesResponse>
}