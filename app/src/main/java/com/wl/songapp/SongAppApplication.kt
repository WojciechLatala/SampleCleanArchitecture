package com.wl.songapp

import android.app.Application
import com.wl.songapp.di.applicationModule
import com.wl.songapp.di.viewModelsModule
import org.koin.android.ext.android.startKoin

class SongAppApplication : Application() {

    companion object {
        lateinit var instance: SongAppApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        startKoin(this, listOf(viewModelsModule, applicationModule))
    }
}