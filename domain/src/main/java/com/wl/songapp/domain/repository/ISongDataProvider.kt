package com.wl.songapp.domain.repository

import com.wl.songapp.domain.entity.SongData
import io.reactivex.Flowable
import io.reactivex.Single

interface ISongDataProvider{

    fun getSongsForArtist(artistName: String): Single<List<SongData>>
    fun getSongsForArtistLocal(artistName: String): Single<List<SongData>>
    fun getSongsForArtistRemote(artistName: String): Single<List<SongData>>
}