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

import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.adapters.RecipeAdapter;
import com.notaprogrammer.baking.model.Recipe;
import com.notaprogrammer.baking.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.notaprogrammer.baking.Constant.PREFERENCE_NAME;
import static com.notaprogrammer.baking.Constant.RECIPE_LOCAL;

public class RecipeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    List<Recipe> recipeList = new ArrayList<>();

    RecyclerView recipeRecyclerView;
    TextView textViewErrorMessage;
    RecipeAdapter recipeAdapter;
    Context context;
    Activity activity;
     SwipeRefreshLayout swipeRefreshLayout;
    public RecipeListFragment(){
        context = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        this.activity = getActivity();
        recipeRecyclerView = rootView.findViewById(R.id.rv_recipes);
        textViewErrorMessage = rootView.findViewById(R.id.tv_error_message);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        recipeAdapter = new RecipeAdapter(recipeList, getActivity());
        recipeRecyclerView.setAdapter(recipeAdapter);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recipeRecyclerView.setLayoutManager(new GridLayoutManager(context, getColumns()));
    }

//    @Override
//    public void onListItemClick(Recipe selectedRecipe) {
//        Intent intent = new Intent(this.getActivity(), StepsListActivity.class);
//        intent.putExtra(StepsListActivity.SELECTED_RECIPE_JSON, new Gson().toJson(selectedRecipe));
//        startActivity(intent);
//    }


    public int getColumns() {
        return getResources().getInteger(R.integer.recipe_columns);
    }

    private void getRecipesFromApi() {

        if(swipeRefreshLayout != null){
            swipeRefreshLayout.setRefreshing(true);
        }


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

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful() && response.body() !=null) {

                    String recipeJsonString = response.body().string();

                    final List<Recipe> recipeList = Recipe.parseJsonList(recipeJsonString);

                    activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(RECIPE_LOCAL, recipeJsonString).apply();

                    for (int i = 0; i < 5; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.interrupted();
                        }
                    }

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            loadRecipeToAdapter(recipeList);
                            //recipeAdapter.updateList(recipeList);
                            ////                            currentMovieList = finalMovieList;
                            //  //                          moviesAdapter.updateList(finalMovieList);
                            //                            //scroll back to the top if user is at the bottom of the screen after sort changes
                            //    //                        moviesRv.smoothScrollToPosition(0);
                            //        //                    if(swipeRefreshLayout != null){
                            //      //                          swipeRefreshLayout.setRefreshing(false);
                            //          //                  }

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

        recipeAdapter.updateList(recipeList);
    }


    @Override
    public void onRefresh() {
        loadRecipe();
    }
}
