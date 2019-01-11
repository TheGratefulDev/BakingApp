package com.notaprogrammer.baking;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;

    private AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
       this.callback = callback;
    }

    void setIsIdleNow(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);

        if(isIdleNow ){
            if(callback!=null){
                callback.onTransitionToIdle();
            }
        }
    }
}
