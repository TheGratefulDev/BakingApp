package com.notaprogrammer.baking.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notaprogrammer.baking.BakingApplication;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.adapters.RecipeListAdapter;
import com.notaprogrammer.baking.model.Recipe;
import com.notaprogrammer.baking.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.notaprogrammer.baking.Constant.PREFERENCE_NAME;
import static com.notaprogrammer.baking.Constant.RECIPE_LOCAL;

public class RecipeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_recipe_list) RecyclerView recipeRecyclerView;
    @BindView(R.id.tv_error_message) TextView textViewErrorMessage;
    @BindView(R.id.srl_recipe_list) SwipeRefreshLayout swipeRefreshLayout;

    List<Recipe> recipeList = new ArrayList<>();
    RecipeListAdapter recipeListAdapter;

    Context context;
    Activity activity;

    public RecipeListFragment(){
        context = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        this.activity = getActivity();

        recipeListAdapter = new RecipeListAdapter(recipeList, getActivity());
        recipeRecyclerView.setAdapter(recipeListAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(context, getColumns()));

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadRecipe();
            }
        });

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(context, getColumns()));
    }

    private void loadRecipe() {
        recipeList = loadRecipeFromLocal();

        if( recipeList == null ){
            getRecipesFromApi();
        }else{
            loadRecipeToAdapter( recipeList );
        }
    }


    private List<Recipe> loadRecipeFromLocal() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE );
        String recipeJson = sharedPreferences.getString(RECIPE_LOCAL, null);

        if(TextUtils.isEmpty(recipeJson)){
            return null;
        }

        return Recipe.parseJsonList(recipeJson);
    }

    public int getColumns() {
        return getResources().getInteger(R.integer.recipe_columns);
    }

    private void getRecipesFromApi() {

        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(true);
        }

        ((BakingApplication) context).setIdleState(true);

        OkHttpClient client = new OkHttpClient();
        HttpUrl url = NetworkUtils.buildRecipeUrl();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayErrorMessage();
                    }
                });
                ((BakingApplication) context).setIdleState(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful() && response.body() !=null) {

                    String recipeJsonString = response.body().string();
                    final List<Recipe> recipeList = Recipe.parseJsonList(recipeJsonString);
                    activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(RECIPE_LOCAL, recipeJsonString).apply();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadRecipeToAdapter(recipeList);
                        }
                    });

                }else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayErrorMessage();
                        }
                    });
                }
                ((BakingApplication) context).setIdleState(false);
            }
        });
    }

    private void displayErrorMessage() {

        textViewErrorMessage.setVisibility(View.VISIBLE);
        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void loadRecipeToAdapter(List<Recipe> recipeList) {

        textViewErrorMessage.setVisibility(View.GONE);
        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(false);
        }
        recipeListAdapter.updateList(recipeList);
    }

    @Override
    public void onRefresh() {
        loadRecipe();
    }
}
