package com.wl.songapp.domain.repository

import com.wl.songapp.domain.entity.SongDataProviderResult
import io.reactivex.Single

interface ISongDataProvider{

    fun getSongsForArtist(artistName: String): Single<SongDataProviderResult>
    fun getSongsForArtistLocal(artistName: String): Single<SongDataProviderResult>
    fun getSongsForArtistRemote(artistName: String): Single<SongDataProviderResult>
}