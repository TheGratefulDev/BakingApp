package com.notaprogrammer.baking;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

public class BakingApplication extends Application {

    @Nullable
    private MyIdlingResource myIdlingResource;

    @VisibleForTesting
    @NonNull
    private IdlingResource initializeIdlingResource() {
        if (myIdlingResource == null) {
            myIdlingResource = new MyIdlingResource();
        }
        return myIdlingResource;
    }

    public BakingApplication() {

        if (BuildConfig.DEBUG) {
            initializeIdlingResource();
        }

    }

    public void setIdleState(boolean state) {
        if (myIdlingResource != null){
            myIdlingResource.setIsIdleNow(state);
        }
    }

    @Nullable
    public MyIdlingResource getIdlingResource() {
        return myIdlingResource;
    }
}
