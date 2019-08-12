package com.wl.songapp.domain.common

import io.reactivex.Single

abstract class Mapper<in E, T> {
    abstract fun map(source: E): T

    fun mapSingle(source: E): Single<T> = Single.just(this.map(source))

    fun mapSingle(source: List<E>): Single<List<T>> = Single.just(source.map { this.map(it) })
}