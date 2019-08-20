package com.wl.songapp.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wl.songapp.data.db.LocalSongProvider
import com.wl.songapp.testAwait
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.koin.core.inject
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class LocalSongProviderTest: KoinTest {

    private val localSongProvider by inject<LocalSongProvider>()

    @Test
    fun getSongsListForEmptyTerm_returnsEmpty(){
        val artistName = ""

        val testObserver = localSongProvider.searchSongsForArtistName(artistName).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(false, response.any())

        println(response)
    }

    @Test
    fun getSongsListForGibberishTerm_returnsEmpty(){
        val artistName = "dahsuofhao oiioas"

        val testObserver = localSongProvider.searchSongsForArtistName(artistName).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(false, response.any())

        println(response)
    }

    @Test
    fun getSongsListForExistingTerm_returnsSongList(){
        val artistName = "38" //from ".38 Special" artist name
        val songsCount = 4

        val testObserver = localSongProvider.searchSongsForArtistName(artistName).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(true, response.any())
        assertEquals(songsCount, response.count())
        println(response)
    }
}