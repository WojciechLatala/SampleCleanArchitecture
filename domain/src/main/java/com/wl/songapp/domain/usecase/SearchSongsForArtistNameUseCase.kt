package com.wl.songapp.domain.usecase

import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Flowable
import io.reactivex.Single

class SearchSongsForArtistNameUseCase(private val songDataProvider: ISongDataProvider) : UseCase() {

    fun search(artistName: String): Single<List<SongData>> {
        return songDataProvider.getSongsForArtist(artistName)
    }

    fun searchFlowable(artistName: String): Flowable<List<SongData>> {
        return songDataProvider.getSongsForArtist(artistName).toFlowable()
    }
}