package com.wl.songapp.data.mapper

import com.wl.songapp.data.entity.SongData
import com.wl.songapp.domain.common.Mapper
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongDataProviderResult
import com.wl.songapp.domain.entity.SongEntity

class SongDataListSongDataProviderResultMapper: Mapper<List<SongData>, SongDataProviderResult>(){

    override fun map(source: List<SongData>): SongDataProviderResult {
        return SongDataProviderResult(source.map {
            SongEntity(
                artistName = it.artistName,
                songTitle = it.trackName,
                releaseYear = it.releaseYear ?: String.empty
            )
        }, null)
    }
}