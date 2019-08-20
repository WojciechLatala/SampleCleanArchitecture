package com.wl.songapp.recyclerview

abstract class RecyclerListItem {

    abstract val itemId: Long
    open fun onBind() {}
    open fun onRecycled() {}

    open fun onSelected() {}

    /**
     * @see android.support.v7.util.DiffUtil.ItemCallback
     */
    open fun isTheSameItemAs(other: RecyclerListItem): Boolean {
        return (this::class == other::class) && itemId == other.itemId
    }

    /**
     * @see android.support.v7.util.DiffUtil.ItemCallback
     */
    open fun hasSameContentAs(other: RecyclerListItem): Boolean {
        return other == this
    }

    companion object {
        const val NO_ID = Long.MIN_VALUE
    }
}