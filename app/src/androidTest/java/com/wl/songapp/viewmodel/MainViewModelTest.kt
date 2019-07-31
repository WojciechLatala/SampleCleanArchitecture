package com.wl.songapp.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.wl.songapp.data.SongDataProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainViewModelTest : KoinTest {

    private val viewModel: MainViewModel = getKoin().get()

    @Test(expected = Exception::class, timeout = 1000)
    fun setInvalidDataSource_throwsException(){
        val initialValue = viewModel.getSongData
        viewModel.setDataSource(-1)
        while (viewModel.getSongData == initialValue){
            //wait for exception or test timeout
        }
    }

    @Test
    fun setDataSourceAsLocal_getSongDataReturnsLocalDataSource(){
        viewModel.setDataSource(MainViewModel.DATA_SOURCE_LOCAL)
        assert(viewModel.getSongData == SongDataProvider::getSongListLocal)
    }

    @Test
    fun setDataSourceAsApi_getSongDataReturnsAPIDataSource(){
        viewModel.setDataSource(MainViewModel.DATA_SOURCE_LOCAL)
        assert(viewModel.getSongData == SongDataProvider::getSongListFromApi)
    }

    @Test
    fun initialDataSourceIsBoth(){
        assert(viewModel.getSongData == SongDataProvider::getSongListFromBoth)
    }

    @Test
    fun getDataForTerm_returnsData() {
        val searchTerm = "ac"

        viewModel.searchTermLiveData.postValue(searchTerm)

        Thread.sleep(MainViewModel.SEARCH_DEBOUNCE_MILLIS + 100) // plus time it takes to update live data

        while(viewModel.isLoading.value == true){
            Thread.sleep(50)
        }

        assert((viewModel.songsListLiveData.value?.count() ?: 0) > 0)
        assert((viewModel.songsListLiveData.value?.first()?.artistName ?: "").contains(searchTerm, true))
    }
}