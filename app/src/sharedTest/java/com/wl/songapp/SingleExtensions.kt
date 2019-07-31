package com.wl.songapp

import io.reactivex.Single
import io.reactivex.observers.TestObserver

fun <T> Single<T>.testAwait(): TestObserver<T> {
    val testObserver = this.test()
    testObserver.awaitTerminalEvent()
    return testObserver
}