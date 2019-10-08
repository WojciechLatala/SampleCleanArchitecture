package com.wl.songapp.viewmodel

import androidx.lifecycle.*
import com.wl.songapp.common.IOThread
import com.wl.songapp.common.IResourceProvider
import com.wl.songapp.common.UIThread
import com.wl.songapp.extension.liveData
import com.wl.songapp.R
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameLocalUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameRemoteUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameUseCase
import com.wl.songapp.entity.SongListItem
import com.wl.songapp.extension.disposeWith
import com.wl.songapp.mapper.SongDataSongListItemMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val resourceProvider: IResourceProvider,
    private val searchSongsForArtistNameLocalUseCase: SearchSongsForArtistNameLocalUseCase,
    private val searchSongsForArtistNameRemoteUseCase: SearchSongsForArtistNameRemoteUseCase,
    private val searchSongsForArtistNameUseCase: SearchSongsForArtistNameUseCase,
    private val songDataSongListItemMapper: SongDataSongListItemMapper
) : BaseViewModel() {

    val searchTermLiveData by liveData(String.empty)

    private val songsListLiveData by liveData<List<SongData>>(emptyList())

    val songsListItems: LiveData<List<SongListItem>> =
        Transformations.map(songsListLiveData) { songList ->
            songList.map { songDataSongListItemMapper.map(it) }
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
            .switchMapSingle<List<SongData>> {
                if (it == String.empty) {
                    Single.just(emptyList())
                } else {
                    getSongData(it)
                }
            }
            .subscribe(::onSongsAcquired, ::onSongsAcquiringError)
            .disposeWith(disposables)
    }

    private fun getSongData(searchTerm: String): Single<List<SongData>> {
        return when (dataSource) {
            DATA_SOURCE_LOCAL -> searchSongsForArtistNameLocalUseCase.search(searchTerm)
            DATA_SOURCE_API -> searchSongsForArtistNameRemoteUseCase.search(searchTerm)
            DATA_SOURCE_BOTH -> searchSongsForArtistNameUseCase.search(searchTerm)
            else -> throw Exception(resourceProvider.getString(R.string.main_activity_invalid_data_source))
        }
    }

    fun setDataSource(dataSource: Int) {
        this.dataSource = dataSource
        onRefresh()
    }

    fun onRefresh() {
        val term = searchTermLiveData.value
        if (term == String.empty) {
            _isLoading.postValue(false)
        } else {
            searchTermLiveData.postValue(term)
        }
    }

    private fun onSongsAcquired(result: List<SongData>) {
        songsListLiveData.postValue(result)
        onSongsAcquiringComplete()
    }

    private fun onSongsAcquiringError(throwable: Throwable) {
        _errorMessage.postValue(throwable.message)
        updateEmptyStateMessage(true)
    }

    private fun onSongsAcquiringComplete(){
        _isLoading.postValue(false)
        if (songsListLiveData.value?.any() != true) {
            updateEmptyStateMessage(false)
        }
    }

    private fun updateEmptyStateMessage(errorOccurred: Boolean) { //todo error handling change
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