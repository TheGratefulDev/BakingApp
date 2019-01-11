package com.notaprogrammer.baking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.notaprogrammer.baking.Constant;
import com.notaprogrammer.baking.Implements.StepListAdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.adapters.StepsListAdapter;
import com.notaprogrammer.baking.feature.widget.BakingWidgetService;
import com.notaprogrammer.baking.fragments.StepDetailFragment;
import com.notaprogrammer.baking.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.notaprogrammer.baking.activities.StepDetailActivity.ARG_ITEMS;
import static com.notaprogrammer.baking.activities.StepDetailActivity.ARG_SELECTED_ITEM_ID;


public class StepListActivity extends AppCompatActivity implements StepListAdapterOnClickInterface {

    public static final String SELECTED_RECIPE_JSON = "SELECTED_RECIPE_JSON";

    @BindView(R.id.toolbar_step_list) Toolbar toolbar;
    @BindView(R.id.rv_step_list) RecyclerView recyclerViewStepList;
    @BindView(R.id.tv_ingredients) TextView textViewIngredients;

    Recipe selectedRecipe;
    String recipeJsonString;

    boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);

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
            toolbar.setTitle(selectedRecipe.getName());
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (findViewById(R.id.container_step_detail) != null) {
                isTwoPane = true;
            }
            setupHeaderView( selectedRecipe );
            setupRecyclerView(recyclerViewStepList, selectedRecipe);

        } else {
            closeActivityAndDisplayErrorToast();
        }
    }


    private void closeActivityAndDisplayErrorToast() {
        Toast.makeText(StepListActivity.this, R.string.problem_loading_recipe, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupHeaderView(Recipe selectedRecipe) {
        Spanned spannedDetailText = Html.fromHtml( selectedRecipe.getIngredientCardDetail()  );
        textViewIngredients.setText(spannedDetailText);
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

            Bundle arguments = StepDetailFragment.stepDetailBundle(selectedRecipe.getSteps().get(step));

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_step_detail, fragment)
                    .commit();

        } else {

            Intent intent = new Intent(this, StepDetailActivity.class);
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

            Toast.makeText(StepListActivity.this, selectedRecipe.getName() + getString(R.string.add_to_widget_message), Toast.LENGTH_SHORT).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

