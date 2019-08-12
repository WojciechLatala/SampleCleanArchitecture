package com.wl.songapp.entity

import com.wl.songapp.recyclerview.RecyclerListItem

data class SongListItem(
    override val itemId: Long,
    val songTitle: String,
    val artistName: String,
    val yearPublished: String
) : RecyclerListItem()