package com.wl.songapp.data

import com.wl.songapp.R
import com.wl.songapp.data.api.ApiSongProvider
import com.wl.songapp.IResourceProvider
import com.wl.songapp.data.local.LocalSongProvider
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SongDataProvider(
    private val resourceProvider: IResourceProvider,
    private val apiDataProvider: ApiSongProvider,
    private val localSongProvider: LocalSongProvider
) {

    fun getSongListLocal(term: String): Single<SongDataProviderResult> {
        return localSongProvider.searchSongsByArtistName(term)
            .map { SongDataProviderResult(it, null) }
            .onErrorReturn { error ->
                SongDataProviderResult(
                    emptyList(),
                    Throwable(
                        resourceProvider.getString(R.string.song_data_provider_local_error)
                    )
                )
            }
    }

    fun getSongListFromApi(term: String): Single<SongDataProviderResult> {
        return apiDataProvider.getITunesResponseForArtistName(term)
            .map { response ->
                SongDataProviderResult(
                    response.results.filter { it.artistName != null && it.trackName != null }.map {
                        Song(
                            artistName = it.artistName!!,
                            songName = it.trackName!!,
                            yearPublished = formatDate(it.releaseDate)
                        )
                    }, null
                )
            }
            .onErrorReturn {
                SongDataProviderResult(
                    emptyList(),
                    Throwable(
                        resourceProvider.getString(
                            if (it is IOException) {
                                R.string.generic_connection_error
                            } else {
                                R.string.song_data_provider_api_error
                            }
                        )
                    )
                )
            }
    }

    fun getSongListFromBoth(term: String): Single<SongDataProviderResult> {
        return getSongListLocal(term)
            .zipWith(
                getSongListFromApi(term),
                BiFunction { local, api ->
                    local.copy(
                        songList = local.songList.plus(api.songList).distinctBy { it.artistName?.toLowerCase() to it.songName?.toLowerCase() },
                        error = mergeErrors(local.error, api.error)
                    )
                })
    }

    private fun mergeErrors(local: Throwable?, api: Throwable?): Throwable? {
        val message = "${local?.message ?: ""} ${api?.message ?: ""}".trim()
        return if (message == "") null else Throwable(message)
    }

    private fun formatDate(date: String?): String {
        if (date == null) return ""
        val parser = SimpleDateFormat(ITUNES_DATE_FORMAT, Locale.US)
        val formatter = SimpleDateFormat("yyyy", Locale.US)
        return formatter.format(parser.parse(date) ?: "")
    }

    companion object {
        private const val ITUNES_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss" // 1980-07-25T07:00:00Z
    }
}