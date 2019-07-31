package com.wl.songapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KProperty

abstract class BaseViewModel : ViewModel() {

    protected val disposables by lazy { CompositeDisposable() }

    private val liveDataObservers by lazy { hashMapOf<Observer<*>, MutableSet<LiveData<out Any>>>() }

    protected fun <T: Any> observe(liveData: LiveData<T>, func: (T) -> Unit){
        observe(liveData, Observer(func))
    }

    private fun <T: Any> observe(liveData: LiveData<T>, observer: Observer<T>){
        liveData.observeForever(observer)
        if (observer !in liveDataObservers){
            liveDataObservers[observer] = mutableSetOf()
        }
        liveDataObservers[observer]!!.add(liveData)
    }

    override fun onCleared() {
        disposables.clear()
        liveDataObservers.forEach { entry ->
            entry.value.forEach {
                @Suppress("UNCHECKED_CAST")
                it.removeObserver(entry.key as Observer<in Any>)
            }
        }
        super.onCleared()
    }

    protected operator fun <V> LiveData<V>.getValue(viewModel: BaseViewModel, property: KProperty<*>): LiveData<V> = this
    protected operator fun <V> MutableLiveData<V>.getValue(viewModel: BaseViewModel, property: KProperty<*>): MutableLiveData<V> = this
}