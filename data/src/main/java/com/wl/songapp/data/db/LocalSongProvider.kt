package com.wl.songapp.data.db

import android.content.Context
import io.reactivex.Single
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import com.wl.songapp.data.entity.SongData

//todo: should be some db, with proper data querying, storage, etc. Here it's a simple json with filter on results for simplicity
class LocalSongProvider(private val context: Context, private val localFilePath: String, private val moshi: Moshi) {

    fun searchSongsForArtistName(artistName: String): Single<List<SongData>> {
        return Single.just(
            if (artistName.isBlank()) {
                emptyList()
            } else {
                getAllSongs().filter {
                    it.artistName.contains(artistName, true)
                }
            }
        )
    }

    private fun getAllSongs(): List<SongData> {
        val jsonString = getLocalAssetString(localFilePath)
        val type = newParameterizedType(List::class.java, SongData::class.java)
        val adapter: JsonAdapter<List<SongData>> = moshi.adapter(type)
        val songs = adapter.fromJson(jsonString)
        return songs ?: emptyList()
    }

    private fun getLocalAssetString(assetPath: String): String {
        return context.assets.open(assetPath).bufferedReader().use {
            it.readText()
        }
    }
}