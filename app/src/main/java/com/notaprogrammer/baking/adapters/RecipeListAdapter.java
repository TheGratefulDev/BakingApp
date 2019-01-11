package com.notaprogrammer.baking.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.notaprogrammer.baking.Implement.RecipeListAdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.model.Recipe;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private RecipeListAdapterOnClickInterface recipeListAdapterOnClickInterface;
    private List<Recipe> recipeList;

    public RecipeListAdapter(List<Recipe> recipeList, Activity activity){
        this.recipeList = recipeList;
        this.recipeListAdapterOnClickInterface = (RecipeListAdapterOnClickInterface) activity;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_view_recipe;

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

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        //TODO ADD IMAGE
        @BindView(R.id.tv_recipe_name)  TextView recipeNameTextView;
        @BindView(R.id.tv_serving) TextView recipeServingTextView;
        @BindView(R.id.iv_recipe_image) ImageView recipeImageView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final Recipe recipe) {
            recipeNameTextView.setText(recipe.getName());

            recipeServingTextView.setText(
                    MessageFormat.format(itemView.getContext().getString(R.string.recipe_card_serving_sufix),
                    recipe.getServings())
            );


            recipeImageView.post(new Runnable() {
                @Override
                public void run() {

                    Picasso.get().load(R.drawable.recipe_place_holder).centerCrop()
                            .fit().into(recipeImageView);

                    if( !TextUtils.isEmpty(recipe.getImage()) ){
                        Picasso.get().load(recipe.getImage()).error(R.drawable.recipe_place_holder).centerCrop().fit().into(recipeImageView);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeListAdapterOnClickInterface.selected(recipe);
                }
            });
        }
    }

    public void updateList(List<Recipe> newRecipeList){
        this.recipeList = newRecipeList;
        notifyDataSetChanged();
    }
}
