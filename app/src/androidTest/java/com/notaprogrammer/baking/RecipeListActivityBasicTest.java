package com.notaprogrammer.baking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.notaprogrammer.baking.activities.RecipeListActivity;
import com.notaprogrammer.baking.activities.StepListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityBasicTest {

    private static final int DEFAULT_POSITION = 0;

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Before
    public void registerIdlingResource() {
        BakingApplication bakingApplication = (BakingApplication) activityTestRule.getActivity().getApplicationContext();

        mIdlingResource = bakingApplication.getIdlingResource();

        //Espresso.registerIdlingResources() is deprecated
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickRecipeList_checkRecipeJson() {
        //Checks if the key is present and bring the data to list Activity
        Intents.init();

        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(DEFAULT_POSITION, click()));

        intended(hasExtraWithKey(StepListActivity.SELECTED_RECIPE_JSON));

        Intents.release();
    }

    @Test
    public void checkWidgetFeature() {
        //Checks if the selected Recipe Json is saved into Shared Preferences
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(DEFAULT_POSITION, click()));

        onView(withId(R.id.action_add_card_to_widget)).check(matches(isDisplayed())).perform(click());

        SharedPreferences sharedPreferences = activityTestRule.getActivity().getApplicationContext().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);

        String widgetRecipeJson = sharedPreferences.getString(Constant.RECIPE_WIDGET, null);

        assertNotNull(widgetRecipeJson);
    }

    @Test
    public void check_recycler_click() {
        //Click through all layouts
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(DEFAULT_POSITION, click()));

        onView(withId(R.id.rv_step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(DEFAULT_POSITION, click()));

        onView(withId(R.id.tv_step_description)).check(matches(isDisplayed()));

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
