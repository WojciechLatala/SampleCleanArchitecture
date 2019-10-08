package com.wl.songapp.data.repository

import com.wl.songapp.data.api.IRemoteSongApi
import com.wl.songapp.data.db.ILocalSongProvider
import com.wl.songapp.data.mapper.ITunesResponseSongEntityListMapper
import com.wl.songapp.data.mapper.SongEntitySongDataMapper
import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class SongDataProvider(
    private val remoteSongProvider: IRemoteSongApi,
    private val localSongProvider: ILocalSongProvider,
    private val iTunesResponseSongEntityListMapper: ITunesResponseSongEntityListMapper,
    private val songEntitySongDataMapper: SongEntitySongDataMapper
) : ISongDataProvider {
    override fun getSongsForArtistLocal(artistName: String): Single<List<SongData>> {
        return Single.just(localSongProvider.getSongsForArtistName(artistName)
//            .map { songsList ->
//                songsList
                .map {
                    songEntitySongDataMapper.map(it) })
//            }
//            .onErrorReturn {
//                SongDataProviderResult(emptyList(), it)
//            }
    }

    override fun getSongsForArtistRemote(artistName: String): Single<List<SongData>> {
        return remoteSongProvider.getSongsForArtistName(artistName)

            .map { iTunesResponseSongEntityListMapper.map(it) }
//            //todo await base update
            .flatMapCompletable { Completable.fromAction { localSongProvider.updateSongsData(it) } }
//            //todo then get new data
            .andThen(getSongsForArtistLocal(artistName))
    }

    override fun getSongsForArtist(artistName: String): Single<List<SongData>> {
        return getSongsForArtistRemote(artistName)
        //todo get somethjing goin on here
//        Flowable.mergeDelayError<List<SongData>> {
//            listOf(
//                getSongsForArtistLocal(artistName),
//                getSongsForArtist(artistName)
//            ).take(2)
//        }
//        return getSongsForArtistLocal(artistName)
//            .zipWith(
//                getSongsForArtistRemote(artistName),
//                BiFunction { localResult, remoteResult ->
//                    SongDataProviderResult(
//                        songList = mergeSongLists(localResult.songList, remoteResult.songList),
//                        error = mergeErrors(localResult.error, remoteResult.error)
//                    )
//                })
    }
//todo remove

//    private fun mergeSongLists(
//        localList: List<SongData>,
//        remoteList: List<SongData>
//    ): List<SongData> {
//        return localList
//            .plus(remoteList)
//            .distinctBy {
//                it.artistName.toLowerCase(Locale.getDefault()) to it.songTitle.toLowerCase(
//                    Locale.getDefault()
//                )
//            }
//    }
//
//    private fun mergeErrors(local: Throwable?, api: Throwable?): Throwable? {
//        val message = "${local?.message ?: String.empty} ${api?.message ?: String.empty}".trim()
//        return if (message == String.empty) null else Throwable(message)
//    }
}