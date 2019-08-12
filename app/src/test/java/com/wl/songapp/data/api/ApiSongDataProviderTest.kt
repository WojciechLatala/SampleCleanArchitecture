package com.wl.songapp.data.api

import com.wl.songapp.di.applicationModule
import com.wl.songapp.extension.empty
import com.wl.songapp.testAwait
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject


class ApiSongDataProviderTest : KoinTest {

    private val apiSongProvider by inject<IRemoteSongApi>()

    @Before
    fun setUp() {
        startKoin{
            listOf(applicationModule)
        }
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `get ITunesResponse with empty term - returns empty ITunesResponse object`(){
        val artistName = String.empty
        val expectedResultCount = 0

        val testObserver = apiSongProvider.getSongsForArtistName(artistName).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(expectedResultCount, response.resultCount)
        println(response)
    }

    @Test
    fun `get ITunesResponse with gibberish term - returns empty ITunesResponse object`(){
        val artistNameTerm = "asdhovuhasoidjnd shadonasid"
        val expectedResultCount = 0
        val testObserver = apiSongProvider.getSongsForArtistName(artistNameTerm).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(expectedResultCount, response.resultCount)
        assertEquals(false, response.results.any())
        println(response)
    }

    @Test
    fun `get ITunesResponse with valid term - returns songs with matching artistName`() {
        val artistNameTerm = "acid drink"
        val expectedResultCount = 50

        val testObserver = apiSongProvider.getSongsForArtistName(artistNameTerm).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(expectedResultCount, response.resultCount)
        assertEquals(true, response.results.first().artistName!!.contains(artistNameTerm, true))
        println(response)
    }
}