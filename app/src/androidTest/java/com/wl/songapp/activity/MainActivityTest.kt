package com.wl.songapp.activity

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.wl.songapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.*


// REMEMBER TO DISABLE THE ANIMATIONS ON THE TESTED DEVICE //
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    private val searchTerm: String = "38 special"
    // both online nad offline results should be mocked in some "mockBuild"
    private val localSongCount: Int = 4

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test(timeout = 70 * 1000) // 60 seconds is standard api request timeout + 10 sec. buffer
    fun searchForArtist_returnsSongsList() {

        onView(withId(R.id.songs_search_input))
            .perform(typeText(searchTerm), closeSoftKeyboard())

        waitForDataToUpdate()

        onView(withId(R.id.songs_recycler))
            .check(RecyclerViewItemCountAssertion(expectedCountGreaterThan = localSongCount))
    }

    @Test(timeout = 70 * 1000) // 60 seconds is standard api request timeout + 10 sec. buffer
    fun switchSourceToLocal_searchForArtist_returnsSongsList() {

        openActionBarOverflowOrOptionsMenu(activityRule.activity)

        onView(withText(R.string.menu_local))
            .perform(click())

        onView(withId(R.id.songs_search_input))
            .perform(typeText(searchTerm), closeSoftKeyboard())

        waitForDataToUpdate()

        onView(withId(R.id.songs_recycler))
            .check(RecyclerViewItemCountAssertion(expectedCountGreaterThan = localSongCount - 1))
    }

    @Test(timeout = 70 * 1000) // 60 seconds is standard api request timeout + 10 sec. buffer
    fun switchSourceToAPI_searchForArtist_returnsSongsList() {

        openActionBarOverflowOrOptionsMenu(activityRule.activity)

        onView(withText(R.string.menu_api))
            .perform(click())

        onView(withId(R.id.songs_search_input))
            .perform(typeText(searchTerm), closeSoftKeyboard())

        waitForDataToUpdate()

        onView(withId(R.id.songs_recycler))
            .check(RecyclerViewItemCountAssertion(expectedCountGreaterThan = localSongCount))
    }

    //ugly await for loading resource
    //integrate idling resource or api mocker build
    private fun waitForDataToUpdate() {
        Thread.sleep(750) // mainViewModel search debounce value

        val swipeRefreshLayout =
            activityRule.activity.findViewById<SwipeRefreshLayout>(R.id.main_activity_swipe_refresh)
        while (swipeRefreshLayout.isRefreshing) {
            Thread.sleep(50)
        }
    }

    class RecyclerViewItemCountAssertion(private val expectedCountGreaterThan: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assert(adapter!!.itemCount > expectedCountGreaterThan)
        }
    }
}