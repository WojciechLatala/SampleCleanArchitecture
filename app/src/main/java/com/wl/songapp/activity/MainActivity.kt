package com.wl.songapp.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wl.songapp.R
import com.wl.songapp.databinding.ActivityMainBinding
import com.wl.songapp.extension.empty
import com.wl.songapp.recyclerview.RecyclerViewAdapter
import com.wl.songapp.entity.SongListItem
import com.wl.songapp.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BindingActivity<ActivityMainBinding>() {

    override val viewModel by viewModel<MainViewModel>()
    private lateinit var recyclerAdapter: RecyclerViewAdapter<SongListItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.createLayoutBinding(R.layout.activity_main)

        setUpToolbar(binding.mainActivityToolbar)
        setUpRecyclerView(binding.songsRecycler)

        observe(viewModel.songsListItems) { recyclerAdapter.data = it }
        observe(viewModel.errorMessage) {
            if(it != String.empty) displayMessage(it)
        }
    }

    private fun setUpToolbar(toolbar: Toolbar) {
        toolbar.title = this.title
        toolbar.inflateMenu(R.menu.main_activity_menu)
        toolbar.setOnMenuItemClickListener(this::onToolbarMenuItemClick)
    }

    private fun onToolbarMenuItemClick(menuItem: MenuItem): Boolean {
        menuItem.isChecked = !menuItem.isChecked
        viewModel.setDataSource(
            when (menuItem.itemId) {
                R.id.main_activity_menu_local -> {
                   MainViewModel.DATA_SOURCE_LOCAL
                }
                R.id.main_activity_menu_api -> {
                    MainViewModel.DATA_SOURCE_API
                }
                R.id.main_activity_menu_both -> {
                    MainViewModel.DATA_SOURCE_BOTH
                }
                else -> return false
            }
        )
        return true
    }

    private fun setUpRecyclerView(recycler: RecyclerView) {
        val viewManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerViewAdapter(R.layout.song_list_item, this)

        recycler.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recyclerAdapter
        }
    }

    private fun displayMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        binding.songsSearchInput.clearFocus()
        super.onPause()
    }
}