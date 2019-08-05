package com.wl.songapp.data.api

import com.wl.songapp.data.api.retrofit.IITunesApi
import io.reactivex.Single

class ApiSongProvider(private val iTunesApiClient: IITunesApi) {

    fun getITunesResponseForArtistName(name: String): Single<ITunesResponse> {
        return iTunesApiClient.getITunesResponseForArtistName(name)
    }
}