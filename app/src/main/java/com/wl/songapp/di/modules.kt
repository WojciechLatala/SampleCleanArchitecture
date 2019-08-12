package com.wl.songapp.di

import android.util.Log
import com.squareup.moshi.Moshi
import com.wl.songapp.AppContext
import com.wl.songapp.IOThread
import com.wl.songapp.IResourceProvider
import com.wl.songapp.data.api.IRemoteSongApi
import com.wl.songapp.data.db.LocalSongProvider
import com.wl.songapp.data.mapper.ITunesResponseSongDataProviderResultMapper
import com.wl.songapp.data.mapper.SongDataListSongDataProviderResultMapper
import com.wl.songapp.data.repository.SongDataProvider
import com.wl.songapp.domain.repository.ISongDataProvider
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameLocalUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameRemoteUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameUseCase
import com.wl.songapp.mapper.SongEntitySongListItemMapper
import com.wl.songapp.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val viewModelsModule = module {

    viewModel {
        MainViewModel(
            resourceProvider = get(),
            searchSongsForArtistNameLocalUseCase = get(),
            searchSongsForArtistNameRemoteUseCase = get(),
            searchSongsForArtistNameUseCase = get(),
            songEntitySongListItemMapper = get()
        )
    }
}

val useCaseModule = module {
    single { SearchSongsForArtistNameLocalUseCase(songDataProvider = get()) }
    single { SearchSongsForArtistNameRemoteUseCase(songDataProvider = get()) }
    single { SearchSongsForArtistNameUseCase(songDataProvider = get()) }
}

val applicationModule = module {

    //resource provider
    single { AppContext(context = get()) } bind IResourceProvider::class

    //OkHttpClient
    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor(get()).apply { level = HttpLoggingInterceptor.Level.BODY })
            .connectTimeout(REQUEST_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(REQUEST_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        HttpLoggingInterceptor.Logger { message ->
            //todo implement timber
            Log.d("OKHTTP", message)
        }
    }

    //Moshi
    single {
        Moshi.Builder()
            .build()
    }

    single { MoshiConverterFactory.create(get()) }

    // Retrofit
    single {
        Retrofit.Builder()
            .client(get()) // OkHttpClient
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(IOThread))
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl(ITUNES_SEARCH_API_URL)
            .build()
    }

    single { get<Retrofit>().create(IRemoteSongApi::class.java) as IRemoteSongApi }
    single { LocalSongProvider(context = get(), localFilePath = SONG_LIST_ASSET_PATH, moshi = get()) }

    single {
        SongDataProvider(
            remoteSongProvider = get(),
            localSongProvider = get(),
            iTunesResponseSongDataProviderResultMapper = get(),
            songDataListSongDataProviderMapper = get()
        ) as ISongDataProvider
    }
}

val mappersModule = module {
    single { ITunesResponseSongDataProviderResultMapper() }
    single { SongDataListSongDataProviderResultMapper() }
    single { SongEntitySongListItemMapper() }
}

private const val REQUEST_CONNECT_TIMEOUT_SECONDS = 60L
private const val REQUEST_READ_TIMEOUT_SECONDS = 60L
private const val REQUEST_WRITE_TIMEOUT_SECONDS = 60L
private const val ITUNES_SEARCH_API_URL = "https://itunes.apple.com/"
private const val SONG_LIST_ASSET_PATH = "json/songs_list"