package com.notaprogrammer.baking.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.notaprogrammer.baking.ItemListActivity;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.dummy.RecipeJson;
import com.notaprogrammer.baking.adapters.RecipeAdapter;
import com.notaprogrammer.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment implements RecipeAdapter.ItemClickListener {

    public static final String ARG_RECIPE = "ARG_RECIPE";
    List<Recipe> recipeList = new ArrayList<>();
    RecyclerView recipeRecyclerView;
    RecipeAdapter recipeAdapter;
    Context context;

    public RecipeListFragment(){
        recipeList = Recipe.parseJsonList(RecipeJson.JSON);
        recipeAdapter = new RecipeAdapter(recipeList, this);
        context = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recipeRecyclerView = rootView.findViewById(R.id.rv_recipes);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(context, getColumns()));
        recipeRecyclerView.setAdapter(recipeAdapter);

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(context, getColumns()));
    }

    @Override
    public void onListItemClick(Recipe selectedRecipe) {
        Intent intent = new Intent(this.getActivity(), ItemListActivity.class);
        intent.putExtra(ItemListActivity.SELECTED_RECIPE_JSON, new Gson().toJson(selectedRecipe));
        startActivity(intent);
        Toast.makeText(getActivity(), selectedRecipe.getName(), Toast.LENGTH_SHORT).show();
    }


    public int getColumns() {
        return getResources().getInteger(R.integer.recipe_columns);
    }
}
