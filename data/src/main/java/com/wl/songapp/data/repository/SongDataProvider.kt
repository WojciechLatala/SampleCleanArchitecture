package com.wl.songapp.data.repository

import com.wl.songapp.data.api.IRemoteSongApi
import com.wl.songapp.data.db.LocalSongProvider
import com.wl.songapp.data.mapper.ITunesResponseSongDataProviderResultMapper
import com.wl.songapp.data.mapper.SongDataListSongDataProviderResultMapper
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongDataProviderResult
import com.wl.songapp.domain.entity.SongEntity
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class SongDataProvider(
    private val remoteSongProvider: IRemoteSongApi,
    private val localSongProvider: LocalSongProvider,
    private val iTunesResponseSongDataProviderResultMapper: ITunesResponseSongDataProviderResultMapper,
    private val songDataListSongDataProviderMapper: SongDataListSongDataProviderResultMapper
) : ISongDataProvider {

    override fun getSongsForArtistLocal(artistName: String): Single<SongDataProviderResult> {
        return localSongProvider.searchSongsForArtistName(artistName)
            .map { songDataListSongDataProviderMapper.map(it) }
            .onErrorReturn {
                SongDataProviderResult(emptyList(), it)
            }
    }

    override fun getSongsForArtistRemote(artistName: String): Single<SongDataProviderResult> {
        return remoteSongProvider.getSongsForArtistName(artistName)
            .map { iTunesResponseSongDataProviderResultMapper.map(it) }
            .onErrorReturn {
                SongDataProviderResult(emptyList(), it)
            }
    }

    override fun getSongsForArtist(artistName: String): Single<SongDataProviderResult> {
        return getSongsForArtistLocal(artistName)
            .zipWith(
                getSongsForArtistRemote(artistName),
                BiFunction { localResult, remoteResult ->
                    SongDataProviderResult(
                        songList = mergeSongLists(localResult.songList, remoteResult.songList),
                        error = mergeErrors(localResult.error, remoteResult.error)
                    )
                })
    }

    private fun mergeSongLists(localList: List<SongEntity>, remoteList: List<SongEntity>): List<SongEntity> {
        return localList
            .plus(remoteList)
            .distinctBy { it.artistName.toLowerCase() to it.songTitle.toLowerCase() }
    }

    private fun mergeErrors(local: Throwable?, api: Throwable?): Throwable? {
        val message = "${local?.message ?: String.empty} ${api?.message ?: String.empty}".trim()
        return if (message == String.empty) null else Throwable(message)
    }
}