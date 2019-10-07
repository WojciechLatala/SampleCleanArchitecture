package com.wl.songapp.data.mapper

import com.wl.songapp.data.entity.ITunesResponse
import com.wl.songapp.data.entity.ITunesResult
import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongData
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Test

class ITunesResponseSongDataProviderResultMapperTest {
    private val iTunesResponseSongDataProviderResultMapper =
        ITunesResponseSongDataProviderResultMapper()

    @Test
    fun `map empty ITunesResponse returns SongDataProviderResult with empty list and no errors`() {
        val iTunesResponse = ITunesResponse(resultCount = 0, results = emptyList())

        val songDataProviderResult = iTunesResponseSongDataProviderResultMapper.map(iTunesResponse)
        assert(!songDataProviderResult.songList.any())
        assertEquals(null, songDataProviderResult.error)
    }

    @Test
    fun `map ITunesResponse returns SongDataProviderResult elements matching input and no errors`() {
        val iTunesResponse = ITunesResponse(
            resultCount = 2,
            results = (0 until 2).map {
                ITunesResult(
                    trackName = "trackName$it",
                    artistName = "artistName$it",
                    releaseDate = "201$it-01-01T12:12:12" //release date format = "yyyy-MM-dd'T'HH:mm:ss"
                )
            }
        )

        val songDataProviderResult = iTunesResponseSongDataProviderResultMapper.map(iTunesResponse)
        assertEquals(2, songDataProviderResult.songList.count())

        assertEquals(
            SongData(
                songTitle = "trackName0",
                artistName = "artistName0",
                releaseYear = "2010"
            ),
            songDataProviderResult.songList[0]
        )
        assertEquals(
            SongData(
                songTitle = "trackName1",
                artistName = "artistName1",
                releaseYear = "2011"
            ),
            songDataProviderResult.songList[1]
        )

        assertEquals(null, songDataProviderResult.error)
    }

    @Test
    fun `map ITunesResponse with null releaseDate returns SongDataProviderResult with empty releaseYear and no errors`() {
        val iTunesResponse = ITunesResponse(
            resultCount = 1,
            results = listOf(
                ITunesResult(trackName = "trackName", artistName = "artistName", releaseDate = null)
            )
        )

        val songDataProviderResult = iTunesResponseSongDataProviderResultMapper.map(iTunesResponse)
        assert(songDataProviderResult.songList.count() == 1)

        assertEquals(
            SongData(
                songTitle = "trackName",
                artistName = "artistName",
                releaseYear = String.empty
            ),
            songDataProviderResult.songList[0]
        )

        assertEquals(null, songDataProviderResult.error)
    }

    @Test
    fun `mapSingle ITunesResponse receive Single with mapped response`() {
        val iTunesResponse = ITunesResponse(
            resultCount = 1,
            results = listOf(ITunesResult(trackName = "trackName", artistName = "artistName", releaseDate = null))
        )

        val mappedSingle = iTunesResponseSongDataProviderResultMapper.mapSingle(iTunesResponse)
        val testObserver = mappedSingle.testAwait()

        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()

        assertEquals(1, response.songList.count())
        assertEquals(
            SongData(
                songTitle = "trackName",
                artistName = "artistName",
                releaseYear = String.empty
            ),
            response.songList[0]
        )
    }
}

fun <T> Single<T>.testAwait(): TestObserver<T> {
    val testObserver = this.test()
    testObserver.awaitTerminalEvent()
    return testObserver
}