package com.wl.songapp.domain.usecase

import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Flowable
import io.reactivex.Single

class SearchSongsForArtistNameRemoteUseCase(private val songDataProvider: ISongDataProvider) : UseCase() {

    fun searchFlowable(artistName: String): Flowable<List<SongData>> {
        return search(artistName).toFlowable()
    }

    fun search(artistName: String): Single<List<SongData>>{
        return songDataProvider.getSongsForArtistRemote(artistName)
    }
}