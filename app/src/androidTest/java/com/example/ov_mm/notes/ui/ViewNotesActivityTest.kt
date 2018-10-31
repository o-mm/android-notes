package com.example.ov_mm.notes.ui

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralLocation
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.example.ov_mm.notes.R
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ViewNotesActivityTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<ViewNotesActivity> = ActivityTestRule(ViewNotesActivity::class.java)


    @Test
    fun noteAdditionTest() {
        val title = UUID.randomUUID().toString()
        val content = UUID.randomUUID().toString()

        //create new not and check it's emptiness
        onView(withId(R.id.add_note_button)).perform(click())
        onView(withId(R.id.title_edit_text)).check(matches(withText("")))
        onView(withId(R.id.content_edit_text)).check(matches(withText("")))

        //type some text and press back
        onView(withId(R.id.title_edit_text)).perform(typeText(title))
        onView(withId(R.id.content_edit_text)).perform(typeText(content))
        Espresso.pressBack() //close keyboard
        Espresso.pressBack()

        val swipeAction = GeneralSwipeAction(Swipe.FAST, GeneralLocation.VISIBLE_CENTER,
            GeneralLocation.TOP_CENTER, Press.FINGER)

        onView(withId(R.id.bottom_sheet_fragment))
            .perform(object: ViewAction {
                override fun getDescription(): String {
                    return swipeAction.description
                }

                override fun getConstraints(): Matcher<View> {
                    return isDisplayingAtLeast(20)
                }

                override fun perform(uiController: UiController?, view: View?) {
                    swipeAction.perform(uiController, view)
                }
            })

        //sort by Date
        val sortByText = "Date"
        onView(withId(R.id.sort_by_spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), equalTo(sortByText))).perform(click(), click())
        onView(withId(R.id.sort_by_spinner)).check(matches(withSpinnerText(sortByText)))

        //choose desc order
        try {
            onView(withId(R.id.sort_order_button)).check(matches(isChecked()))
        } catch (e: AssertionFailedError) {
            onView(withId(R.id.sort_order_button)).perform(click())
        }

        //check the top item in recycler view
        onView(withId(R.id.note_recycle_view))
            .perform(RecyclerViewActions.scrollToPosition<NoteRecyclerViewAdapter.ViewHolder>(0))
        onView(withText(title)).check(matches(isDisplayed()))
        onView(withText(content)).check(matches(isDisplayed()))
    }
}
