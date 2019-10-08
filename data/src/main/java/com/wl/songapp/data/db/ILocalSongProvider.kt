package com.wl.songapp.data.db

import io.reactivex.Single
import com.wl.songapp.data.entity.SongEntity

interface ILocalSongProvider{
    fun getSongsForArtistName(artistName: String): List<SongEntity>

    fun updateSongsData(songEntities: List<SongEntity>)
}