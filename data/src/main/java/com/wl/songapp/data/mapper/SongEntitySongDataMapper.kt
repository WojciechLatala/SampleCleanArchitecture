package com.wl.songapp.data.mapper

import com.wl.songapp.data.entity.SongEntity
import com.wl.songapp.domain.common.Mapper
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongData

class SongEntitySongDataMapper : Mapper<SongEntity, SongData>() {
    override fun map(source: SongEntity): SongData {
        return SongData(
            songId = source.trackId,
            songTitle = source.trackName,
            artistName = source.artistName,
            releaseYear = source.releaseYear ?: String.empty
        )
    }
}