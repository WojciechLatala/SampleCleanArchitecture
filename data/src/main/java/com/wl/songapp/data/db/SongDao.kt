package com.wl.songapp.data.db

import androidx.room.Dao
import androidx.room.Query
import com.wl.songapp.data.entity.SongEntity
import io.reactivex.Single

@Dao
interface SongDao : BaseDao<SongEntity>, ILocalSongProvider {

    @Query("SELECT * FROM songs")
    fun getSongs(): Single<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE artistName LIKE :artistName")
    override fun getSongsForArtistName(artistName: String): Single<List<SongEntity>>
}