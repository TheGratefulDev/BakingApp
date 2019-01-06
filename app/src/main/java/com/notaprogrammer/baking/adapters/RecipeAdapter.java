package com.notaprogrammer.baking.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ItemClickListener onListItemClick;
    private List<Recipe> recipeList;

    public interface ItemClickListener {
        void onListItemClick(Recipe selectedRecipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, ItemClickListener listener){
        this.recipeList = recipeList;
        this.onListItemClick = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_recipe;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        Recipe recipe = recipeList.get(position);
        recipeViewHolder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView recipeNameTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.textView_recipe_name);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
           recipeNameTextView.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Recipe selectedRecipe = recipeList.get(clickedPosition);
            onListItemClick.onListItemClick(selectedRecipe);
        }
    }

    public void updateList(List<Recipe> recipeList){
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }
}
