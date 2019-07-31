package com.wl.songapp.extensions

import androidx.lifecycle.*
import kotlin.reflect.KProperty

operator fun <T : Any> MutableLiveData<T>.getValue(thisRef: Any, property: KProperty<*>): MutableLiveData<T> = this
fun <T : Any> liveData(initialValue: T): MutableLiveData<T> = MutableLiveData<T>().apply { postValue(initialValue) }