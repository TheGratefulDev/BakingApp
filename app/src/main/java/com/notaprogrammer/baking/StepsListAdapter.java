package com.notaprogrammer.baking;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.notaprogrammer.baking.Implement.AdapterOnClickInterface;
import com.notaprogrammer.baking.model.Recipe;

import java.util.List;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {

    private int selectedId = -1;
    private AdapterOnClickInterface listener;
    private List<Recipe.Step> stepList;
    private final boolean isTwoPane;

    StepsListAdapter(StepsListActivity parent, Recipe items, boolean twoPane) {

        try {
            this.listener = parent;
        } catch (ClassCastException e) {
            throw new ClassCastException("StepsListActivity must implement AdapterCallback.");
        }

        stepList = items.getSteps();
        isTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        Recipe.Step selectedStep = stepList.get(position);

        holder.idView.setText(String.valueOf( position ));
        holder.contentView.setText(selectedStep.getShortDescription());
        holder.itemView.setTag(selectedStep);

        if(isTwoPane){
            holder.itemView.setBackgroundColor(selectedId == position ? Color.GRAY : Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView idView;
        final TextView contentView;

        ViewHolder(View view) {
            super(view);

            idView = view.findViewById(R.id.id_text);
            contentView = view.findViewById(R.id.content);

        }
    }
}