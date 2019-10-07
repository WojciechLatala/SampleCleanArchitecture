package com.wl.songapp.domain.usecase

import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Flowable

class SearchSongsForArtistNameUseCase(private val songDataProvider: ISongDataProvider) : UseCase() {

    fun search(artistName: String): Flowable<List<SongData>> {
        return songDataProvider.getSongsForArtist(artistName)
    }
}