package com.wl.songapp.common

/**
 * This is an utility class implementing [Double-checked locking](https://en.wikipedia.org/wiki/Double-checked_locking) to create an instance of singleton object.
 * It is based on the JVM implementation of Kotlin's lazy() function.
 */

internal open class SingletonHolder<in A, out T>(factory: (A) -> T) {
    private var factory: ((A) -> T)? = factory
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val localInstance = instance
        if (localInstance != null) {
            return localInstance
        }

        return synchronized(this) {
            val safeInstance = instance
            if (safeInstance != null) {
                safeInstance
            } else {
                val newInstance = factory!!(arg)
                instance = newInstance
                factory = null // clear factory lambda as it will never be used again
                newInstance
            }
        }
    }
}