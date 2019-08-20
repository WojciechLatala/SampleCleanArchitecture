package com.wl.songapp.domain.usecase

import com.wl.songapp.domain.entity.SongDataProviderResult
import com.wl.songapp.domain.repository.ISongDataProvider
import io.reactivex.Single

class SearchSongsForArtistNameUseCase(private val songDataProvider: ISongDataProvider) : UseCase() {

    fun search(artistName: String): Single<SongDataProviderResult>{
        return songDataProvider.getSongsForArtist(artistName)
    }
}