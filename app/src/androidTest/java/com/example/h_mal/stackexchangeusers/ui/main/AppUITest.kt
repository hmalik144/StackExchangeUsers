package com.example.h_mal.stackexchangeusers.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.h_mal.stackexchangeusers.R
import com.example.h_mal.stackexchangeusers.application.AppClass.Companion.idlingResources
import com.example.h_mal.stackexchangeusers.data.preferences.PreferenceProvider
import com.example.h_mal.stackexchangeusers.data.room.AppDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    val users = setOf("kenny", "adam", "gary", "user")

    lateinit var currentUser: String

    @Before
    fun setUp() = runBlocking{
        deletePreviousEntries()
    }

    private suspend fun deletePreviousEntries(){
        idlingResources.increment()
        AppDatabase.invoke(InstrumentationRegistry.getInstrumentation().context).getUsersDao().deleteEntries()
        idlingResources.decrement()
    }

    @Test
    fun mainActivityTest() {
        val user = users.random()

        onView(allOf(
                withId(R.id.search_button), withContentDescription("Search"),
                isDisplayed()
            )
        ).perform(click())

        onView(allOf(withId(R.id.search_src_text), isDisplayed()))
            .perform(replaceText(user), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.search_src_text), withText(user),
                isDisplayed()
            )
        ).perform(pressImeActionButton())

        val viewGroup = onView(
            allOf(childAtPosition(allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(withId(R.id.main), 1)
                    ), 0),
                isDisplayed()
            )
        )
        onView(isRoot()).perform(waitFor(2000))
        viewGroup.check(matches(isDisplayed()))
    }

    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
