package com.notaprogrammer.baking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private final ItemListActivity parentActivity;
    Recipe recipe;
    private List<Recipe.Step> stepList;
    private final boolean isTwoPane;

    RecipeStepsAdapter(ItemListActivity parent, Recipe items, boolean twoPane) {
        recipe = items;
        stepList = recipe.getSteps();
        parentActivity = parent;
        isTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf( stepList.get(position).getId()) );
        holder.mContentView.setText(stepList.get(position).getShortDescription());
        holder.itemView.setTag(stepList.get(position));
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Recipe.Step step = (Recipe.Step) view.getTag();

            if (isTwoPane) {

                Bundle arguments = new Bundle();

                arguments.putString(ItemDetailFragment.ARG_ITEM_ID, step.toJsonString());
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                parentActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();

            } else {

                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, step.toJsonString());

                context.startActivity(intent);

            }
        }
    };


}