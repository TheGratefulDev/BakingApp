package com.notaprogrammer.baking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeListFragment recipeListFragment = new RecipeListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        fragmentManager.beginTransaction().add(R.id.container, recipeListFragment).commit();
    }
}
