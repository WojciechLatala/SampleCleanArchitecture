package com.wl.songapp.recyclerview

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.*
import java.lang.ref.WeakReference

typealias ViewHolderFactory = (parent: ViewGroup, viewType: Int, lifecycleOwner: LifecycleOwner) -> RecyclerViewHolder

open class RecyclerViewAdapter<TListItem : RecyclerListItem>(
    private val vhFactoryFunction: ViewHolderFactory, lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerViewHolder>(), RecyclerViewHolder.IOnClickListener {

    constructor(@LayoutRes layoutId: Int, lifecycleOwner: LifecycleOwner) : this(SimpleViewHolderFactory(layoutId), lifecycleOwner)
    constructor(vhFactory: IViewHolderFactory, lifecycleOwner: LifecycleOwner) : this(vhFactory::createViewHolder, lifecycleOwner)

    private val weakLifecycleOwner = WeakReference(lifecycleOwner)

    open var data: List<TListItem>
        get() = asyncListDiffer.currentList
        set(newData) = asyncListDiffer.submitList(newData)

    private val asyncListDiffer by lazy { createAsyncListDiffer() }

    private var clickListener: IListItemClickListener? = null

    fun setOnListItemClickListener(clickListener: IListItemClickListener?) {
        this.clickListener = clickListener
    }

    private fun createAsyncListDiffer(): AsyncListDiffer<TListItem> {
        val updateCallback = createListUpdateCallback()
        val config: AsyncDifferConfig<TListItem> = createAsyncDifferConfig()
        return AsyncListDiffer(updateCallback, config)
    }

    protected open fun createListUpdateCallback(): ListUpdateCallback {
        return AdapterListUpdateCallback(this)
    }
    protected open fun createAsyncDifferConfig(): AsyncDifferConfig<TListItem>{
        return AsyncDifferConfig.Builder(createDiffUtilItemCallback()).build()
    }

    protected open fun createDiffUtilItemCallback(): DiffUtil.ItemCallback<TListItem> {
        return object : DiffUtil.ItemCallback<TListItem>() {
            override fun areItemsTheSame(oldItem: TListItem, newItem: TListItem): Boolean {
                return newItem.isTheSameItemAs(oldItem)
            }

            override fun areContentsTheSame(oldItem: TListItem, newItem: TListItem): Boolean {
                return newItem.hasSameContentAs(oldItem)
            }
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return vhFactoryFunction.invoke(parent, viewType, weakLifecycleOwner.get()!!)
    }

    override fun getItemCount() = data.size

    override fun getItemId(position: Int): Long {
        val id = data[position].itemId
        return if (id == RecyclerListItem.NO_ID) {
            RecyclerView.NO_ID
        } else {
            id
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, this)
    }

    override fun onClicked(viewHolder: RecyclerViewHolder) {
        clickListener?.onListItemClicked(viewHolder.boundItem!!)
    }


    override fun onViewAttachedToWindow(holder: RecyclerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }

    override fun onViewRecycled(holder: RecyclerViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled()
    }

    override fun onFailedToRecycleView(holder: RecyclerViewHolder): Boolean {
        holder.itemView.clearAnimation()
        return super.onFailedToRecycleView(holder)
    }

    private class SimpleViewHolderFactory(private val layoutId: Int) : IViewHolderFactory {
        override fun createViewHolder(parent: ViewGroup, viewType: Int, lifecycleOwner: LifecycleOwner): RecyclerViewHolder {
            return RecyclerViewHolder(layoutId, parent, lifecycleOwner)
        }
    }
}