package com.wl.songapp.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wl.songapp.testAwait
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.koin.core.get
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class LocalSongProviderTest : KoinTest {
    //todo room database test
//
//    private val localSongProvider = LocalSongProvider(get(), "json/songs_list", get())
//
//    @Test
//    fun getSongDataListForEmptyTerm_returnsEmpty() {
//        val artistName = ""
//
//        val testObserver = localSongProvider.getSongsForArtistName(artistName).testAwait()
//        testObserver.assertNoErrors()
//        testObserver.assertNoTimeout()
//        testObserver.assertValueCount(1)
//        val response = testObserver.values().first()
//        assertEquals(false, response.any())
//
//        println(response)
//    }
//
//    @Test
//    fun getSongDataListForGibberishTerm_returnsEmpty() {
//        val artistName = "dahsuofhao oiioas"
//
//        val testObserver = localSongProvider.getSongsForArtistName(artistName).testAwait()
//        testObserver.assertNoErrors()
//        testObserver.assertNoTimeout()
//        testObserver.assertValueCount(1)
//        val response = testObserver.values().first()
//        assertEquals(false, response.any())
//
//        println(response)
//    }
//
//    @Test
//    fun getSongDataListForValidTerm_returnsSongList() {
//        val artistName = "38" //from ".38 Special" artist name present in the file
//        val songsCount = 4
//
//        val testObserver = localSongProvider.getSongsForArtistName(artistName).testAwait()
//        testObserver.assertNoErrors()
//        testObserver.assertNoTimeout()
//        testObserver.assertValueCount(1)
//        val response = testObserver.values().first()
//        assertEquals(true, response.any())
//        assertEquals(songsCount, response.count())
//        println(response)
//    }
}