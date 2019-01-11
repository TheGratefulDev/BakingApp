package com.notaprogrammer.baking.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.fragments.StepDetailFragment;
import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ARG_ITEMS = "ARG_ITEMS";
    public static final String ARG_SELECTED_ITEM_ID = "ARG_SELECTED_ITEM_ID";

    @BindView(R.id.btn_previous_step) Button previousButton;
    @BindView(R.id.btn_next_step) Button nextButton;
    @BindView(R.id.btn_step_detail) Toolbar toolbar;

    List<Recipe.Step> stepList;
    int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            int selectedPosition = getIntent().getIntExtra(ARG_SELECTED_ITEM_ID, 0);

            String recipeJson = getIntent().getStringExtra(ARG_ITEMS);
            stepList = Recipe.parseJsonObject(recipeJson).getSteps();

            updateFragment(selectedPosition);
        }
    }

    public void updateFragment(int currentSelectedPosition){

        selectedPosition = currentSelectedPosition;

        enablePreviousButton(currentSelectedPosition -1 > - 1 );
        enableNextButton(currentSelectedPosition + 1 < stepList.size() );

        Bundle arguments = StepDetailFragment.stepDetailBundle( stepList.get(currentSelectedPosition) );

        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_step_detail, fragment).commit();
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

        int nextPosition = selectedPosition + 1;
        int previousPosition = selectedPosition - 1;

        if( v.getId()== R.id.btn_next_step){
            updateFragment( nextPosition );
        }

        if( v.getId() == R.id.btn_previous_step){
            updateFragment( previousPosition);
        }

    }

    private void enablePreviousButton(boolean displayPreviousButton) {
        previousButton.setEnabled(displayPreviousButton);
    }

    private void enableNextButton( boolean displayNextButton) {
        nextButton.setEnabled(displayNextButton);
    }

}
