package com.wl.songapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey @ColumnInfo(name = "id") val trackId: Long,
    val trackName: String,
    val artistName: String,
    val releaseYear: String?
)