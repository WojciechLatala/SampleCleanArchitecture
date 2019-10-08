package com.wl.songapp.data.db

import androidx.room.Dao
import androidx.room.Query
import com.wl.songapp.data.entity.SongEntity
import io.reactivex.Single

@Dao
interface SongDao : BaseDao<SongEntity> {

    @Query("SELECT * FROM songs")
    fun getSongs(): List<SongEntity>

    @Query("SELECT * FROM songs WHERE artistName LIKE :artistName")
    fun getSongsForArtistName(artistName: String): List<SongEntity>
}