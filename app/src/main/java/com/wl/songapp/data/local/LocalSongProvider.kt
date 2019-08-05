package com.wl.songapp.data.local

import android.content.Context
import com.wl.songapp.data.Song
import io.reactivex.Single
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType

//Should be db, with proper data querying. Here it's a simple json with filter on results for simplicity
class LocalSongProvider(private val context: Context, private val moshi: Moshi) {

    fun searchSongsByArtistName(artistName: String): Single<List<Song>> {
        return Single.just(
            if (artistName == "") {
                emptyList()
            } else {
                getAllSongs().filter {
                    it.artistName.contains(artistName, true)
                }
            }
        )
    }

    private fun getAllSongs(): List<Song> {
        val jsonString = getLocalAssetString(SONG_LIST_ASSET_PATH)
        val type = newParameterizedType(List::class.java, Song::class.java)
        val adapter: JsonAdapter<List<Song>> = moshi.adapter(type)
        val songs = adapter.fromJson(jsonString)
        return songs ?: emptyList()
    }

    private fun getLocalAssetString(assetPath: String): String {
        return context.assets.open(assetPath).bufferedReader().use {
            it.readText()
        }
    }

    companion object {
        private const val SONG_LIST_ASSET_PATH = "json/songs_list"
    }
}