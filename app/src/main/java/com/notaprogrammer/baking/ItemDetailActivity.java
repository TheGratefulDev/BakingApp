package com.notaprogrammer.baking;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Button previousButton;
    Button nextButton;
    String currentSelectedJson;
    String recipeJson;
    List<Recipe.Step> stepList;
    Recipe.Step currentStep;

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

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            currentSelectedJson = getIntent().getStringExtra(ItemDetailFragment.ARG_SELECTED_ITEM);
            recipeJson = getIntent().getStringExtra(ItemDetailFragment.ARG_ITEMS);
            stepList = Recipe.parseJsonObject(recipeJson).getSteps();
            updateFragment(currentSelectedJson);
        }
    }

    public void updateFragment(String selectedStepJson){

        currentStep = new Gson().fromJson(selectedStepJson, Recipe.Step.class);

        updateNavigateButtonUi(currentStep.getId()-1> - 1 , currentStep.getId() + 1 < stepList.size() );

        Bundle arguments = new Bundle();
        arguments.putString(ItemDetailFragment.ARG_SELECTED_ITEM, selectedStepJson);
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

        int nextPosition = currentStep.getId()+1;
        int previousPosition = currentStep.getId()-1;

        if( v.getId()== R.id.next_step_button ){
            updateFragment( findStepById(nextPosition) );
        }

        if( v.getId() == R.id.previous_step_button ){
            updateFragment( findStepById(previousPosition) );
        }

    }

    private void updateNavigateButtonUi(boolean displayPreviousButton, boolean displayNextButton) {
        previousButton.setEnabled(displayPreviousButton);
        nextButton.setEnabled(displayNextButton);
    }

    private String findStepById(int id){
        for (Recipe.Step step : stepList){
            if(step.getId() == id ){
                return new Gson().toJson(step);
            }
        }
        return null;
    }
}
