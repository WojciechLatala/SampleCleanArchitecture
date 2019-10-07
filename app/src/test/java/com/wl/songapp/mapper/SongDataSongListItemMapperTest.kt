package com.wl.songapp.mapper

import com.wl.songapp.domain.common.empty
import com.wl.songapp.domain.entity.SongData
import com.wl.songapp.recyclerview.RecyclerListItem
import com.wl.songapp.testAwait
import org.junit.Test

class SongDataSongListItemMapperTest{

    private val songEntitySongListItemMapper = SongDataSongListItemMapper()

    private fun getSongEntity(nameSuffix: String = String.empty): SongData {
        return SongData("song_title_$nameSuffix", "artist_name_$nameSuffix", "release_year_$nameSuffix")
    }

    @Test
    fun `map SongEntity returns SongListItem with no id`(){
        val songEntity = getSongEntity()

        val songListItem = songEntitySongListItemMapper.map(songEntity)

        assert(songListItem.artistName == songEntity.artistName )
        assert(songListItem.songTitle == songEntity.songTitle )
        assert(songListItem.yearPublished == songEntity.releaseYear )
        assert(songListItem.itemId == RecyclerListItem.NO_ID)
    }

    @Test
    fun `mapSingle SongEntity returns Single SongListItem with no id`(){
        val songEntity = getSongEntity()

        val songListItemSingle = songEntitySongListItemMapper.mapSingle(songEntity)

        val testObserver = songListItemSingle.testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()
        assert(response.artistName == songEntity.artistName )
        assert(response.songTitle == songEntity.songTitle )
        assert(response.yearPublished == songEntity.releaseYear )
        assert(response.itemId == RecyclerListItem.NO_ID)
    }

    @Test
    fun `mapSingle list of SongEntity returns Single List of SongListItem with no id`(){
        val songEntity1 = getSongEntity("1")
        val songEntity2 = getSongEntity("2")
        val songEntityList = listOf(songEntity1, songEntity2)

        val songListItemSingle = songEntitySongListItemMapper.mapSingle(songEntityList)

        val testObserver = songListItemSingle.testAwait()
        testObserver.assertNoErrors()
        testObserver.assertNoTimeout()
        testObserver.assertValueCount(1)

        val response = testObserver.values().first()

        assert(response.count() == 2)

        val songListItem1 = response[0]
        val songListItem2 = response[1]

        assert(songListItem1.artistName == songEntity1.artistName )
        assert(songListItem1.songTitle == songEntity1.songTitle )
        assert(songListItem1.yearPublished == songEntity1.releaseYear )
        assert(songListItem1.itemId == RecyclerListItem.NO_ID)

        assert(songListItem2.artistName == songEntity2.artistName )
        assert(songListItem2.songTitle == songEntity2.songTitle )
        assert(songListItem2.yearPublished == songEntity2.releaseYear )
        assert(songListItem2.itemId == RecyclerListItem.NO_ID)
    }
}