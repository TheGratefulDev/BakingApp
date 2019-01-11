package com.notaprogrammer.baking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.notaprogrammer.baking.Implement.RecipeListAdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.fragments.RecipeListFragment;
import com.notaprogrammer.baking.model.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapterOnClickInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_recipe_list, new RecipeListFragment())
                    .commit();
        }
    }

    @Override
    public void selected(Recipe recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(StepListActivity.SELECTED_RECIPE_JSON, new Gson().toJson(recipe));
        startActivity(intent);
    }
}
