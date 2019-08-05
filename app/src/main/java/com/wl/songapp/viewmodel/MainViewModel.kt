package com.wl.songapp.viewmodel

import androidx.lifecycle.*
import com.wl.songapp.IOThread
import com.wl.songapp.IResourceProvider
import com.wl.songapp.UIThread
import com.wl.songapp.extensions.disposeWith
import com.wl.songapp.extensions.liveData
import com.wl.songapp.data.Song
import com.wl.songapp.data.SongDataProvider
import com.wl.songapp.data.SongDataProviderResult
import com.wl.songapp.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val resourceProvider: IResourceProvider,
    private val songDataDataProvider: SongDataProvider
) : BaseViewModel() {

    val searchTermLiveData by liveData("")

    private val _songsListLiveData by liveData<List<Song>>(emptyList())
    val songsListLiveData get() = _songsListLiveData

    private val _isLoading by liveData(false)
    val isLoading get() = _isLoading

    private val dataSource by liveData(DATA_SOURCE_BOTH)

    private val _errorMessage by liveData("")
    val errorMessage get() = _errorMessage

    val emptyStateVisible = Transformations.map(_songsListLiveData) { !it.any() }!!

    private val _emptyStateText by liveData("")
    val emptyStateText get() = _emptyStateText

    init {
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
            .skipWhile { it == "" }
            .distinctUntilChanged()
            .debounce(SEARCH_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
            .doOnNext {
                _isLoading.postValue(true)
            }
            .switchMapSingle<SongDataProviderResult> {
                if (it == "") {
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
        get() = when (dataSource.value) {
            DATA_SOURCE_LOCAL -> songDataDataProvider::getSongListLocal
            DATA_SOURCE_API -> songDataDataProvider::getSongListFromApi
            DATA_SOURCE_BOTH -> songDataDataProvider::getSongListFromBoth
            else -> throw Exception(resourceProvider.getString(R.string.main_activity_invalid_data_source))
        }

    fun setDataSource(dataSource: Int) {
        this.dataSource.postValue(dataSource)
        onRefresh()
    }

    fun onRefresh() {
        val term = searchTermLiveData.value
        if(term == ""){
            _isLoading.postValue(false)
            return
        }
        searchTermLiveData.value = ""
        searchTermLiveData.value = term
    }

    private fun onSongsAcquired(result: SongDataProviderResult) {
        _isLoading.postValue(false)
        _songsListLiveData.postValue(result.songList)
        if (result.error != null) {
            onSongsAcquiringError(result.error)
        }

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