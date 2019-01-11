package com.notaprogrammer.baking;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.notaprogrammer.baking.activities.RecipeListActivity;
import com.notaprogrammer.baking.adapters.RecipeListAdapter;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityBasicTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule = new ActivityTestRule<>(RecipeListActivity.class);


    private static Matcher<RecipeListAdapter.RecipeViewHolder> isInTheMiddle() {
        return new TypeSafeMatcher<RecipeListAdapter.RecipeViewHolder>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {

            }

            @Override
            protected boolean matchesSafely(RecipeListAdapter.RecipeViewHolder item) {
                return false;
            }

        };
    }


}
