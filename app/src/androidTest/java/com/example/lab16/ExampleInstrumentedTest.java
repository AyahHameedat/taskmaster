package com.example.lab16;

import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;

import android.app.Activity;
import android.support.test.espresso.ViewAction;
//import android.support.test.espresso.contrib.RecyclerViewActions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
//import android.support.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.lab16.data.TaskData;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest<ActivityTestRule> {


    @Rule
    public ActivityScenarioRule rule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.lab16", appContext.getPackageName());
    }

    @Test
    public void validateUITest() {
        onView(withId(R.id.MY_TASKs)).check(matches((withText("My Tasks"))));
        onView(withId(R.id.txt_username)).check(matches(withText("NewT")));
        onView(withId(R.id.imageView)).check(matches((isDisplayed())));

    }


    @Test
    public void navigateToSettingsScreenTest() {

        onView(withId(R.id.btn_Settings)).perform(click());

        onView(withId(R.id.edit_text_username_setting)).perform(typeText("NewT"),
                closeSoftKeyboard());
        onView(withId(R.id.btn_save)).perform(click());

        onView(withId(R.id.txt_username)).check(matches((withText("NewT"))));

    }


    @Test
    public void navigateToTasksScreenTest()
    {


        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.Title_taskDetails)).check(matches(withText("TitleAYA2")));

    }

    @Test
    public void AddTaskScreenTest() {

        onView(withId(R.id.ADD_TASK)).perform(click());

        onView(withId(R.id.ADD_TITLE)).perform(typeText("AYA2"),
                closeSoftKeyboard());
        onView(withId(R.id.ADD_BODY)).perform(typeText("AYA2............."),
                closeSoftKeyboard());
        onView(withId(R.id.ADD_SUBMIT)).perform(click());

//        List<TaskData> taskList = AppDatabase.getInstance(getApplicationContext()).taskDao().getAll();

    }
}


