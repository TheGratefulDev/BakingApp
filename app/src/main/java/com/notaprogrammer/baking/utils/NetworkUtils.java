package com.notaprogrammer.baking.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

import okhttp3.HttpUrl;

public class NetworkUtils {

    private static final String HTTPS = "https";
    private static final String RECIPES_SERVER = "d17h27t6h515a5.cloudfront.net";
    private static final String TOPHER = "topher";
    private static final String YEAR = "2017";
    private static final String MONTH = "May";
    private static final String FILE_PATH = "59121517_baking";
    private static final String FILE = "baking.json";


    public static HttpUrl buildRecipeUrl() {

        return new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(RECIPES_SERVER)
                .addPathSegment(TOPHER)
                .addPathSegment(YEAR)
                .addPathSegment(MONTH)
                .addPathSegment(FILE_PATH)
                .addPathSegment(FILE)
                .build();
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}

