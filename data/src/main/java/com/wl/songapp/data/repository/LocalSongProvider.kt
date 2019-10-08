package com.wl.songapp.data.repository

import com.wl.songapp.data.db.ILocalSongProvider
import com.wl.songapp.data.db.SongDao
import com.wl.songapp.data.entity.SongEntity
import io.reactivex.Single

class LocalSongProvider(private val songDao: SongDao) : ILocalSongProvider {
    override fun getSongsForArtistName(artistName: String): List<SongEntity> {
        return songDao.getSongsForArtistName(artistName)
    }

    override fun updateSongsData(songEntities: List<SongEntity>) {
        songDao.update(songEntities)
    }
}