package com.wl.songapp.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.wl.songapp.R
import com.wl.songapp.data.Song

//should be generic adapter with data binding
class SongAdapter(private var data: List<Song>) : RecyclerView.Adapter<SongViewHolder>() {

    fun updateData(songsList: List<Song>) {
        data = songsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_list_item, parent, false) as ConstraintLayout
        return SongViewHolder(layout)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = data[position]
        holder.view.findViewById<TextView>(R.id.song_title).text = song.songName
        holder.view.findViewById<TextView>(R.id.song_artist).text = song.artistName
        val yearPublishedTextView = holder.view.findViewById<TextView>(R.id.song_year)
        if (song.yearPublished == "") {
            yearPublishedTextView.visibility = View.INVISIBLE
        } else {
            yearPublishedTextView.visibility = View.VISIBLE
        }
        yearPublishedTextView.text = song.yearPublished
    }

    override fun getItemCount() = data.size
}