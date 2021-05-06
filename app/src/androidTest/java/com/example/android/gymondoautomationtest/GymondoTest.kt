package com.example.android.gymondoautomationtest

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dalvik.annotation.TestTarget
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Pattern.matches


@RunWith(AndroidJUnit4::class)
@LargeTest
class GymondoTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val username = "automation@gymondo.de"
    private val password = "automation"
    private val searchWord = "walk"

    public override fun setUp() {
        super.setUp()
    }


    @Test
    fun user_login(){

        //Enter username
        Espresso.onView(withId(R.id.editText)).perform(typeText(username));
        Espresso.closeSoftKeyboard();

        //Enter password
        Espresso.onView(withId(R.id.editText2)).perform(typeText(password));
        Espresso.closeSoftKeyboard();

        //Click login
        Espresso.onView(withId(R.id.button)).perform(click());

    }

    @Test
    fun search_from_list(){

        //Enter username
        Espresso.onView(withId(R.id.editTxtSearch)).perform(typeText(searchWord));
        Espresso.closeSoftKeyboard();

        //Click search button
        Espresso.onView(withId(R.id.btnSearch)).perform(click());

        //onView(withText("Walk")).check(matches(isDisplayed()))

    }


    public override fun tearDown() {


    }
}

