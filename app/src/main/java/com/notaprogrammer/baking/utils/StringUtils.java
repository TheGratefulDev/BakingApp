package com.notaprogrammer.baking.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class StringUtils {

    private static final String UTF8 = "UTF-8";

    public static String getUTF8DecodedDescription(String text){

        if(TextUtils.isEmpty(text) ){
            return "";
        }

        try {
            return URLDecoder.decode(text,UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return text;
    }
}
