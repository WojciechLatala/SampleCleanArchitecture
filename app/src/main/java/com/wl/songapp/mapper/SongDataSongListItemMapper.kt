package com.wl.songapp.mapper

import com.wl.songapp.domain.common.Mapper
import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.entity.SongListItem
import com.wl.songapp.recyclerview.RecyclerListItem.Companion.NO_ID

class SongDataSongListItemMapper : Mapper<SongData, SongListItem>() {

    override fun map(source: SongData): SongListItem {
        return SongListItem(
            itemId = NO_ID,
            songTitle = source.songTitle,
            artistName = source.artistName,
            yearPublished = source.releaseYear
        )
    }
}