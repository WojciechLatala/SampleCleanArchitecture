package com.wl.songapp.data.db

import io.reactivex.Single
import com.wl.songapp.data.entity.SongData

//todo: should be some db, with proper data querying, storage, etc. For now a simple interface with only GET
interface ILocalSongProvider{
    fun getSongsForArtistName(artistName: String): Single<List<SongData>>
}