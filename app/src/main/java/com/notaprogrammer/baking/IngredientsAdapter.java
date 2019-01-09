package com.notaprogrammer.baking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class IngredientsAdapter extends ArrayAdapter<Recipe.Ingredient> {

    IngredientsAdapter(Context context, List<Recipe.Ingredient> ingredients) {
        super(context, 0, ingredients);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Recipe.Ingredient ingredient = getItem(position);
        if(ingredient!=null){
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_ingredient, parent, false);
            }

            TextView textViewIngredient = convertView.findViewById(R.id.textView_ingredient);
            textViewIngredient.setText(ingredient.getReadableString());
        }

        return convertView;
    }
}
