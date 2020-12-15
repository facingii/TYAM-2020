package com.outlook.gonzasosa.apps.espressouitest;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ensureTextChangesWork () {
        onView (withId (R.id.inputField)).perform (typeText ("HELLO"), ViewActions.closeSoftKeyboard ());
        onView (withId (R.id.changeText)).perform (click ());
        onView (withId (R.id.inputField)).check (matches (withText ("Leia Princess")));
    }

    @Test
    public void changeText_NewActivity () {
        onView (withId (R.id.inputField)).perform (typeText ("NewText"), ViewActions.closeSoftKeyboard ());
        onView (withId (R.id.switchActivity)).perform (click ());
        onView (withId (R.id.resultView)).check (matches (withText ("NewText")));
    }

}
