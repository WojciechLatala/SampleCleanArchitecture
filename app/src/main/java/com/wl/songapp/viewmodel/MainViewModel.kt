package com.wl.songapp.viewmodel

import androidx.lifecycle.*
import com.wl.songapp.IOThread
import com.wl.songapp.IResourceProvider
import com.wl.songapp.UIThread
import com.wl.songapp.extension.liveData
import com.wl.songapp.R
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongDataProviderResult
import com.wl.songapp.domain.entity.SongEntity
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameLocalUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameRemoteUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameUseCase
import com.wl.songapp.entity.SongListItem
import com.wl.songapp.extension.disposeWith
import com.wl.songapp.mapper.SongEntitySongListItemMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val resourceProvider: IResourceProvider,
    private val searchSongsForArtistNameLocalUseCase: SearchSongsForArtistNameLocalUseCase,
    private val searchSongsForArtistNameRemoteUseCase: SearchSongsForArtistNameRemoteUseCase,
    private val searchSongsForArtistNameUseCase: SearchSongsForArtistNameUseCase,
    private val songEntitySongListItemMapper: SongEntitySongListItemMapper
) : BaseViewModel() {

    val searchTermLiveData by liveData(String.empty)

    private val songsListLiveData by liveData<List<SongEntity>>(emptyList())

    val songsListItems: LiveData<List<SongListItem>> = Transformations.map(songsListLiveData) { songList ->
        songList.map { songEntitySongListItemMapper.map(it) }
    }

    private val _isLoading by liveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var dataSource: Int = DATA_SOURCE_BOTH

    private val _errorMessage by liveData(String.empty)
    val errorMessage: LiveData<String> get() = _errorMessage

    val emptyStateVisible = Transformations.map(songsListLiveData) { !it.any() }!!

    private val _emptyStateText by liveData(String.empty)
    val emptyStateText: LiveData<String> get() = _emptyStateText

    init {
        createInputFlowable()
    }

    private fun createInputFlowable() {
        Flowable.create<String>({ emitter ->
            val observer = Observer<String> {
                if (it != null && !emitter.isCancelled) {
                    emitter.onNext(it)
                }
            }
            searchTermLiveData.observeForever(observer)
            emitter.setCancellable { searchTermLiveData.removeObserver(observer) }
        }, BackpressureStrategy.LATEST)
            .subscribeOn(UIThread)
            .observeOn(IOThread)
            .skipWhile { it == String.empty }
            .debounce(SEARCH_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
            .doOnNext {
                _isLoading.postValue(true)
            }
            .switchMapSingle<SongDataProviderResult> {
                if (it == String.empty) {
                    Single.just(SongDataProviderResult(emptyList(), null))
                } else {
                    getSongData(it)
                        .onErrorReturn { error -> SongDataProviderResult(emptyList(), error) }
                }
            }
            .subscribe(::onSongsAcquired, ::onSongsAcquiringError)
            .disposeWith(disposables)
    }

    val getSongData: ((String) -> Single<SongDataProviderResult>)
        get() = when (dataSource) {
            DATA_SOURCE_LOCAL -> searchSongsForArtistNameLocalUseCase::search
            DATA_SOURCE_API -> searchSongsForArtistNameRemoteUseCase::search
            DATA_SOURCE_BOTH -> searchSongsForArtistNameUseCase::search
            else -> throw Exception(resourceProvider.getString(R.string.main_activity_invalid_data_source))
        }

    fun setDataSource(dataSource: Int) {
        this.dataSource = dataSource
        onRefresh()
    }

    fun onRefresh() {
        val term = searchTermLiveData.value
        if(term == String.empty){
            _isLoading.postValue(false)
        }
        else{
            searchTermLiveData.postValue(term)
        }
    }

    private fun onSongsAcquired(result: SongDataProviderResult) {
        _isLoading.postValue(false)
        songsListLiveData.postValue(result.songList)

        result.error?.let { onSongsAcquiringError(it) }

        if (!result.songList.any()) {
            updateEmptyStateMessage(result.error != null)
        }
    }

    private fun onSongsAcquiringError(throwable: Throwable) {
        _errorMessage.postValue(throwable.message)
    }

    private fun updateEmptyStateMessage(errorOccurred: Boolean) {
        _emptyStateText.postValue(
            resourceProvider.getString(
                if (errorOccurred) {
                    R.string.main_activity_data_fetch_error
                } else {
                    R.string.main_activity_no_songs_found
                }
            )
        )
    }

    companion object {
        const val DATA_SOURCE_LOCAL = 0
        const val DATA_SOURCE_API = 1
        const val DATA_SOURCE_BOTH = 2

        const val SEARCH_DEBOUNCE_MILLIS = 700L
    }
}