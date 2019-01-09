package com.notaprogrammer.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.notaprogrammer.baking.Implement.RecipeAdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.StepsListActivity;
import com.notaprogrammer.baking.fragments.RecipeListFragment;
import com.notaprogrammer.baking.model.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeAdapterOnClickInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new RecipeListFragment())
                    .commit();
        }
    }

    @Override
    public void selected(Recipe recipe) {
        Intent intent = new Intent(this, StepsListActivity.class);
        intent.putExtra(StepsListActivity.SELECTED_RECIPE_JSON, new Gson().toJson(recipe));
        startActivity(intent);
    }


}
