package com.wl.songapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wl.songapp.data.entity.SongEntity

@Database(entities = [SongEntity::class], version = 1)
abstract class SongsDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
}