package com.wl.songapp.data.mapper

import com.wl.songapp.data.entity.ITunesResponse
import com.wl.songapp.domain.common.Mapper
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongEntity
import com.wl.songapp.domain.entity.SongDataProviderResult
import java.text.SimpleDateFormat
import java.util.*

class ITunesResponseSongDataProviderResultMapper : Mapper<ITunesResponse, SongDataProviderResult>() {
    override fun map(source: ITunesResponse): SongDataProviderResult {
        return SongDataProviderResult(
            songList = source.results.map {
                SongEntity(
                    artistName = it.artistName,
                    songTitle = it.trackName,
                    releaseYear = formatDate(it.releaseDate)
                )
            },
            error = null
        )
    }

    private fun formatDate(date: String?): String {
        if (date == null) return String.empty
        val parser = SimpleDateFormat(ITUNES_DATE_FORMAT, Locale.US)
        val formatter = SimpleDateFormat("yyyy", Locale.US)
        return formatter.format(parser.parse(date) ?: String.empty)
    }

    companion object {
        private const val ITUNES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss" // 1980-07-25T07:00:00Z
    }
}