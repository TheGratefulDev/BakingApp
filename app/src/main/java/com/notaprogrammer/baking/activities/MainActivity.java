package com.notaprogrammer.baking.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.notaprogrammer.baking.AdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.dummy.RecipeJson;
import com.notaprogrammer.baking.fragments.RecipeListFragment;

public class MainActivity extends AppCompatActivity implements AdapterOnClickInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RecipeListFragment.ARG_RECIPE, RecipeJson.JSON);

            RecipeListFragment fragment = new RecipeListFragment();

            fragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public void selected(int position) {

    }
}
