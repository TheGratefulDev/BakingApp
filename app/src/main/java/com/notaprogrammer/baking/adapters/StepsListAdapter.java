package com.notaprogrammer.baking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notaprogrammer.baking.Implements.StepListAdapterOnClickInterface;
import com.notaprogrammer.baking.R;
import com.notaprogrammer.baking.activities.StepListActivity;
import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepViewHolder> {

    private StepListAdapterOnClickInterface listener;
    private List<Recipe.Step> stepList;
    private final boolean isTwoPane;
    private int selectedId = -1;

    public StepsListAdapter(StepListActivity parent, Recipe items, boolean twoPane) {

        try {
            this.listener = parent;
        } catch (ClassCastException e) {
            throw new ClassCastException("StepListActivity must implement AdapterCallback.");
        }

        stepList = items.getSteps();
        isTwoPane = twoPane;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.view_step;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Recipe.Step selectedStep = stepList.get(position);
        holder.bind( selectedStep, position);
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_id) TextView textViewStepPosition;
        @BindView(R.id.tv_step_name) TextView textViewStepName;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Recipe.Step selectedStep, final int position) {

            textViewStepPosition.setText( String.valueOf( position ) );
            textViewStepName.setText(selectedStep.getShortDescription());
            itemView.setTag(selectedStep);

            if(isTwoPane){
                itemView.setBackgroundColor(selectedId == position ? Color.GRAY : Color.TRANSPARENT);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isTwoPane) {

                        notifyItemChanged(selectedId);
                        selectedId = position;
                        notifyItemChanged(selectedId);
                    }

                    listener.selected(position);
                }
            });
        }

    }
}