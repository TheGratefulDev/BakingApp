package com.notaprogrammer.baking;

import android.os.Bundle;

import com.notaprogrammer.baking.model.Recipe;

public class ItemDetailUtils {

    public static Bundle detailBundle(Recipe.Step step){
        Bundle arguments = new Bundle();

        arguments.putString(ItemDetailFragment.ARG_SELECTED_ITEM, step.toJsonString());

        return arguments;
    }

}
