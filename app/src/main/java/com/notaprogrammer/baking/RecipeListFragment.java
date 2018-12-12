package com.notaprogrammer.baking;

import android.content.Context;
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
import com.google.gson.reflect.TypeToken;
import com.notaprogrammer.baking.obj.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment implements RecipeAdapter.ItemClickListener {

    List<Recipe> recipeList = new ArrayList<>();
    RecyclerView recipeRecyclerView;
    RecipeAdapter recipeAdapter;
    Context context;

    public RecipeListFragment(){
        recipeList = new Gson().fromJson(RecipeJson.JSON, new TypeToken<List<Recipe>>(){}.getType());
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
        Toast.makeText(getActivity(), selectedRecipe.getName(), Toast.LENGTH_SHORT).show();
    }


    public int getColumns() {
        return getResources().getInteger(R.integer.recipe_columns);
    }
}
