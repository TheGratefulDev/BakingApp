package com.notaprogrammer.baking;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ARG_ITEMS = "ARG_ITEMS";
    public static final String ARG_SELECTED_ITEM_ID = "ARG_SELECTED_ITEM_ID";

    Button previousButton;
    Button nextButton;
    String recipeJson;
    List<Recipe.Step> stepList;
    int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        nextButton = findViewById(R.id.next_step_button);
        previousButton = findViewById(R.id.previous_step_button);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            int selectedPosition = getIntent().getIntExtra(ARG_SELECTED_ITEM_ID, 0);
            recipeJson = getIntent().getStringExtra(ARG_ITEMS);
            stepList = Recipe.parseJsonObject(recipeJson).getSteps();

            updateFragment(selectedPosition);
        }
    }

    public void updateFragment(int currentSelectedPosition){

        selectedPosition = currentSelectedPosition;

        updateNavigateButtonUi(currentSelectedPosition-1> - 1 , currentSelectedPosition + 1 < stepList.size() );

        Bundle arguments = ItemDetailUtils.detailBundle(stepList.get(currentSelectedPosition));

        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int nextPosition = selectedPosition+1;
        int previousPosition = selectedPosition-1;

        if( v.getId()== R.id.next_step_button ){
            updateFragment( nextPosition );
        }

        if( v.getId() == R.id.previous_step_button ){
            updateFragment( previousPosition);
        }

    }

    private void updateNavigateButtonUi(boolean displayPreviousButton, boolean displayNextButton) {
        previousButton.setEnabled(displayPreviousButton);
        nextButton.setEnabled(displayNextButton);
    }
}
