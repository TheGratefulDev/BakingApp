package com.notaprogrammer.baking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.notaprogrammer.baking.Implement.AdapterOnClickInterface;
import com.notaprogrammer.baking.model.Recipe;
import com.notaprogrammer.baking.utils.DetailUtils;

import static com.notaprogrammer.baking.DetailActivity.ARG_ITEMS;
import static com.notaprogrammer.baking.DetailActivity.ARG_SELECTED_ITEM_ID;


public class StepsListActivity extends AppCompatActivity implements AdapterOnClickInterface {

    public static final String SELECTED_RECIPE_JSON = "SELECTED_RECIPE_JSON";
    private String recipeJsonString;
    private Recipe selectedRecipe;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        if (savedInstanceState != null) {
            recipeJsonString = savedInstanceState.getString(SELECTED_RECIPE_JSON);
        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(SELECTED_RECIPE_JSON)) {
                recipeJsonString = intent.getStringExtra(SELECTED_RECIPE_JSON);
            }
        }

        if (!TextUtils.isEmpty(recipeJsonString)) {

            selectedRecipe = Recipe.parseJsonObject(recipeJsonString);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(selectedRecipe.getName());
            setSupportActionBar(toolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (findViewById(R.id.item_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                isTwoPane = true;
            }


            View headerView = findViewById(R.id.card_view_ingredients);
            assert headerView != null;
            setupHeaderView((CardView) headerView, selectedRecipe );

            View recyclerView = findViewById(R.id.item_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView, selectedRecipe);

        } else {
            closeActivityAndDisplayErrorToast();
        }
    }

    private void closeActivityAndDisplayErrorToast() {
        Toast.makeText(StepsListActivity.this, R.string.problem_loading_recipe, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpFromSameTask(this);
            return true;

        }else if(id == R.id.action_add_card_to_widget){

            SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putString(Constant.RECIPE_WIDGET, selectedRecipe.toJsonString()).apply();
            BakingWidgetService.updateWidget(this);

            Toast.makeText(StepsListActivity.this, selectedRecipe.getName() + " ingredient card has been removed from the Widget", Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupHeaderView(CardView headerView, Recipe selectedRecipe) {
        Spanned spanned = Html.fromHtml( selectedRecipe.getIngredientCardDetail()  );
        ((TextView) headerView.findViewById(R.id.textView_ingredients)).setText( spanned );
      //  ListView listView = headerView.findViewById(R.id.listView_ingredients);
      //  listView.setAdapter(new IngredientsAdapter(this.getApplicationContext(), selectedRecipe.getIngredients()));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, Recipe selectedRecipe) {
        recyclerView.setAdapter(new StepsListAdapter(this, selectedRecipe, isTwoPane));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SELECTED_RECIPE_JSON, selectedRecipe.toJsonString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void selected(int step) {

        if (isTwoPane) {

            Bundle arguments = DetailUtils.detailBundle(selectedRecipe.getSteps().get(step));

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(ARG_SELECTED_ITEM_ID, step);
            intent.putExtra(ARG_ITEMS, selectedRecipe.toJsonString());
            startActivity(intent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.step_list_menu, menu);
        return true;
    }

}

