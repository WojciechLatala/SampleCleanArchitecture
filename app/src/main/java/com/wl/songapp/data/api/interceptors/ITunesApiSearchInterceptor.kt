package com.wl.songapp.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ITunesApiSearchInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url().newBuilder()
            .addQueryParameter("entity", "song")
            .addQueryParameter("attribute", "artistTerm")
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}