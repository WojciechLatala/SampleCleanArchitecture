package com.wl.songapp.data.local

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squareup.moshi.Moshi
import com.wl.songapp.SongAppApplication
import com.wl.songapp.testAwait
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalSongProviderTest {

    private val localSongProvider = LocalSongProvider(ApplicationProvider.getApplicationContext<SongAppApplication>(), Moshi.Builder().build())

    @Test
    fun getSongsListForEmptyTerm_returnsEmpty(){
        val artistName = ""

        val testObserver = localSongProvider.searchSongsByArtistName(artistName).testAwait()
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

        val testObserver = localSongProvider.searchSongsByArtistName(artistName).testAwait()
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

        val testObserver = localSongProvider.searchSongsByArtistName(artistName).testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)
        val response = testObserver.values().first()
        assertEquals(true, response.any())
        assertEquals(songsCount, response.count())
        println(response)
    }
}