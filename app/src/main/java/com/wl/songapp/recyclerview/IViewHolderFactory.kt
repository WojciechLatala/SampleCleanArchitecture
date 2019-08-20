package com.wl.songapp.recyclerview

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

interface IViewHolderFactory {
    fun createViewHolder(parent: ViewGroup, viewType: Int, lifecycleOwner: LifecycleOwner): RecyclerViewHolder
}