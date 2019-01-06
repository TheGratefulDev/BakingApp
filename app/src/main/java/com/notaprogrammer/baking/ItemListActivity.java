package com.notaprogrammer.baking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.notaprogrammer.baking.model.Recipe;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    public static final String SELECTED_RECIPE_JSON = "SELECTED_RECIPE_JSON";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;
    private Recipe selectedRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Intent intent = getIntent();

        if( intent == null || !intent.hasExtra(SELECTED_RECIPE_JSON)){
            //error handle
            closeActivityAndDisplayErrorToast();

        }else{
            String recipeJsonString = intent.getStringExtra(SELECTED_RECIPE_JSON);
            selectedRecipe = Recipe.parseJsonObject(recipeJsonString);

            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(selectedRecipe.getName());
            setSupportActionBar(toolbar);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

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

            View recyclerView = findViewById(R.id.item_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView, selectedRecipe);

        }
    }

    private void closeActivityAndDisplayErrorToast() {
        Toast.makeText(ItemListActivity.this, R.string.problem_loading_recipe , Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, Recipe selectedRecipe) {
        recyclerView.setAdapter(new RecipeStepsAdapter(this, selectedRecipe, isTwoPane));
    }

}
