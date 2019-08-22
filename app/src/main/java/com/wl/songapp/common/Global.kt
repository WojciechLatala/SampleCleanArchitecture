package com.wl.songapp.common

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val UIThread: Scheduler get() = AndroidSchedulers.mainThread()
val IOThread: Scheduler get() = Schedulers.io()