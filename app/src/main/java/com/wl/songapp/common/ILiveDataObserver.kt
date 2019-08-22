package com.wl.songapp.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

interface ILiveDataObserver : LifecycleOwner {

    fun <T> observe(liveData: LiveData<T>, observerLambda: (T) -> Unit) {
        liveData.observe(this, Observer<T> { t -> t?.let(observerLambda) })
    }

    fun <T> Fragment.observeOnView(liveData: LiveData<T>, observerLambda: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer<T> { t -> t?.let(observerLambda) })
    }
}