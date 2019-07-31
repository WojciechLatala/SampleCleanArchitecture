package com.wl.songapp.data.api.interceptors

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

import org.junit.Before

class ITunesApiSearchInterceptorTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            start()
            enqueue(MockResponse())
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `make request with ITunesApiSearchInterceptor - url has added additional query parameters`() {
        val interceptor = ITunesApiSearchInterceptor()

        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        okHttpClient.newCall(Request.Builder().url(mockWebServer.url(".../search?term=testSearchTerm")).build())
            .execute()

        val request = mockWebServer.takeRequest()
        assert(request.path.contains("entity=song"))
        assert(request.path.contains("attribute=artistTerm"))
    }
}