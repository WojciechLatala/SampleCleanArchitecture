package com.wl.songapp.di
//
//import android.content.Context
//import com.squareup.moshi.JsonAdapter
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.Types
//import com.wl.songapp.data.db.ILocalSongProvider
//import com.wl.songapp.data.entity.SongEntity
//import io.reactivex.Single
//
////todo remove from here, create Room DB instance and Room DB files in Data module
//class LocalSongProvider(private val context: Context, private val localFilePath: String, private val moshi: Moshi):
//    ILocalSongProvider {
//
//    override fun getSongsForArtistName(artistName: String): Single<List<SongEntity>> {
//        return Single.just(
//            if (artistName.isBlank()) {
//                emptyList()
//            } else {
//                getAllSongs().filter {
//                    it.artistName.contains(artistName, true)
//                }
//            }
//        )
//    }
//
//    private fun getAllSongs(): List<SongData> {
//        val jsonString = getLocalAssetString(localFilePath)
//        val type = Types.newParameterizedType(List::class.java, SongData::class.java)
//        val adapter: JsonAdapter<List<SongData>> = moshi.adapter(type)
//        val songs = adapter.fromJson(jsonString)
//        return songs ?: emptyList()
//    }
//
//    private fun getLocalAssetString(assetPath: String): String {
//        return context.assets.open(assetPath).bufferedReader().use {
//            it.readText()
//        }
//    }
//}