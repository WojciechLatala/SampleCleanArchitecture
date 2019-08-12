package com.wl.songapp

import android.app.Application
import com.wl.songapp.di.applicationModule
import com.wl.songapp.di.mappersModule
import com.wl.songapp.di.useCaseModule
import com.wl.songapp.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class SongAppApplication : Application() {

    companion object {
        lateinit var instance: SongAppApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        startKoin {
            androidLogger()
            androidContext(instance)
            modules(listOf(viewModelsModule, useCaseModule, mappersModule, applicationModule))
        }
    }
}