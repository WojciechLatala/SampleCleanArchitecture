package com.wl.songapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.wl.songapp.BR

open class RecyclerViewHolder(private val binding: ViewDataBinding) :
    androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    constructor(
        layoutId: Int,
        parent: ViewGroup,
        lifecycleOwner: LifecycleOwner
    ) : this((Companion::getBindingForLayoutId)(layoutId, parent, lifecycleOwner))

    var boundItem: RecyclerListItem? = null
    private var clickListener: IOnClickListener? = null

    open fun onClicked() {
        clickListener?.onClicked(this)
    }

    open fun <T : RecyclerListItem> bind(viewModel: T, clickListener: IOnClickListener? = null) {
        if (!binding.setVariable(BR.viewModel, viewModel)) {
            throw IllegalStateException("Binding $binding viewModel variable name should be 'viewModel'")
        }
        binding.executePendingBindings()
        viewModel.onBind()
        this.boundItem = viewModel
        if (clickListener != null) {
            this.clickListener = clickListener
            binding.root.setOnClickListener { onClicked() }
        }
    }

    internal fun onAttached() {

    }

    internal fun onDetached() {
    }

    internal fun onRecycled() {
        binding.root.setOnClickListener(null)
        this.clickListener = null
        boundItem?.onRecycled()
        boundItem = null
    }

    companion object {
        fun getBindingForLayoutId(layoutId: Int, parent: ViewGroup, lifecycleOwner: LifecycleOwner): ViewDataBinding {
            val layoutInflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false).apply {
                setLifecycleOwner(lifecycleOwner)
            }
        }
    }

    interface IOnClickListener {
        fun onClicked(viewHolder: RecyclerViewHolder)
    }
}