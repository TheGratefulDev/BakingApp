package com.notaprogrammer.baking.feature.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.notaprogrammer.baking.Constant;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class BakingWidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Recipe.Ingredient> ingredients;

    BakingWidgetListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String recipeJson = sharedPreferences.getString(Constant.RECIPE_WIDGET, null );

        Recipe recipe = Recipe.parseJsonObject(recipeJson);
        if(recipe == null){
            ingredients = new ArrayList<>();
        }else{
            ingredients = recipe.getIngredients();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.view_ingredient);

        row.setTextViewText(R.id.textView_ingredient, ingredients.get(position).getReadableString() );

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
