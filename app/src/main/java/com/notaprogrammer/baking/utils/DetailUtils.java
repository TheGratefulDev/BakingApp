package com.notaprogrammer.baking.utils;

import android.os.Bundle;

import com.notaprogrammer.baking.DetailFragment;
import com.notaprogrammer.baking.model.Recipe;

public class DetailUtils {

    public static Bundle detailBundle(Recipe.Step step){

        Bundle arguments = new Bundle();

        arguments.putString(DetailFragment.ARG_SELECTED_ITEM, step.toJsonString());

        return arguments;
    }

}
