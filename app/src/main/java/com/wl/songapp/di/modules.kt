package com.wl.songapp.di

import android.util.Log
import com.squareup.moshi.Moshi
import com.wl.songapp.AppContext
import com.wl.songapp.IOThread
import com.wl.songapp.IResourceProvider
import com.wl.songapp.data.api.interceptors.ITunesApiSearchInterceptor
import com.wl.songapp.data.api.ApiSongProvider
import com.wl.songapp.data.local.LocalSongProvider
import com.wl.songapp.data.SongDataProvider
import com.wl.songapp.data.api.retrofit.IITunesApi
import com.wl.songapp.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val viewModelsModule = module {

    viewModel { MainViewModel(get(), get()) }
}

val applicationModule = module {

    //resource provider
    single { AppContext(get()) } bind IResourceProvider::class

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
            .client(
                (get() as OkHttpClient)
                    .newBuilder()
                    .addNetworkInterceptor(ITunesApiSearchInterceptor())
                    .build()
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(IOThread))
            .addConverterFactory(get<MoshiConverterFactory>())
            .baseUrl(ITUNES_SEARCH_API_URL)
            .build()
    }

    single { get<Retrofit>().create(IITunesApi::class.java) as IITunesApi }

    single { SongDataProvider(get(), get(), get()) }
    single { ApiSongProvider(get()) }
    single { LocalSongProvider(get(), get()) }
}

private const val REQUEST_CONNECT_TIMEOUT_SECONDS = 60L
private const val REQUEST_READ_TIMEOUT_SECONDS = 60L
private const val REQUEST_WRITE_TIMEOUT_SECONDS = 60L
private const val ITUNES_SEARCH_API_URL = "https://itunes.apple.com/"