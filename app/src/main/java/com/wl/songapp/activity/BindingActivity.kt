package com.wl.songapp.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wl.songapp.BR
import com.wl.songapp.ILiveDataObserver
import com.wl.songapp.R
import com.wl.songapp.viewmodel.BaseViewModel

abstract class BindingActivity<TBinding : ViewDataBinding> :
    AppCompatActivity(), ILiveDataObserver {

    protected abstract val viewModel: BaseViewModel
    private var _binding: TBinding? = null
    protected val binding: TBinding get() = _binding ?: throwBindingError()

    protected fun createLayoutBinding(layoutResId: Int) {
        _binding = setBindingContentView(layoutResId)
    }

    private fun <T : ViewDataBinding> setBindingContentView(resId: Int): T =
        DataBindingUtil.setContentView<T>(this, resId)
            .apply {
                lifecycleOwner = this@BindingActivity
                setVariable(BR.viewModel, viewModel)
            }

    override fun onDestroy() {
        _binding?.unbind()
        _binding = null
        super.onDestroy()
    }

    private fun throwBindingError(): Nothing {
        throw IllegalStateException(resources.getText(R.string.illegal_binding).toString())
    }
}