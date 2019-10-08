package com.wl.songapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.wl.songapp.common.AppContext
import com.wl.songapp.common.IOThread
import com.wl.songapp.common.IResourceProvider
import com.wl.songapp.common.SingletonHolder
import com.wl.songapp.data.api.IRemoteSongApi
import com.wl.songapp.data.db.ILocalSongProvider
import com.wl.songapp.data.db.SongsDatabase
import com.wl.songapp.data.mapper.ITunesResponseSongEntityListMapper
import com.wl.songapp.data.mapper.SongEntitySongDataMapper
import com.wl.songapp.data.repository.LocalSongProvider
import com.wl.songapp.data.repository.SongDataProvider
import com.wl.songapp.domain.repository.ISongDataProvider
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameLocalUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameRemoteUseCase
import com.wl.songapp.domain.usecase.SearchSongsForArtistNameUseCase
import com.wl.songapp.mapper.SongDataSongListItemMapper
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
            songDataSongListItemMapper = get()
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
            .addNetworkInterceptor(HttpLoggingInterceptor(get()).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
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

    //data access objects
    single { get<Retrofit>().create(IRemoteSongApi::class.java) as IRemoteSongApi }
    factory { get<SongsDatabase>().songDao() }
    single { LocalSongProvider(songDao = get()) as ILocalSongProvider}

    //database
    single {
        SingletonHolder<Context, SongsDatabase> {
            Room.databaseBuilder(
                it.applicationContext,
                SongsDatabase::class.java,
                "songs.db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }.getInstance(get())
    }

    single {
        SongDataProvider(
            remoteSongProvider = get(),
            localSongProvider = get(),
            iTunesResponseSongEntityListMapper = get(),
            songEntitySongDataMapper = get()
        ) as ISongDataProvider
    }
}

val mappersModule = module {
    single { SongEntitySongDataMapper() }
    single { ITunesResponseSongEntityListMapper() }
    single { SongDataSongListItemMapper() }
}

private const val REQUEST_CONNECT_TIMEOUT_SECONDS = 60L
private const val REQUEST_READ_TIMEOUT_SECONDS = 60L
private const val REQUEST_WRITE_TIMEOUT_SECONDS = 60L
private const val ITUNES_SEARCH_API_URL = "https://itunes.apple.com/"