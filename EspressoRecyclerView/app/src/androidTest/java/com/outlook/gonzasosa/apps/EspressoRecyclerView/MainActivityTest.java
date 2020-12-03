package com.outlook.gonzasosa.apps.EspressoRecyclerView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith (AndroidJUnit4.class)
public class MainActivityTest {
  @Rule
  public ActivityScenarioRule<MainActivity> activityScenarioRule =
          new ActivityScenarioRule<MainActivity>(MainActivity.class);
  @Test
  public void clickItem () {

    onView (withId (R.id.recycler_view))
            .perform (RecyclerViewActions.actionOnItemAtPosition (45, click ()));

    onView (withId (R.id.text))
            .check (matches (withText ("45")))
            .check (matches (isDisplayed ()));

  }

}