package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.newsapp.utils.Constants;

public final class UrlQueryParameter{

    public static Uri.Builder makeQuery(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortBy = sharedPreferences.getString(
                context.getString(R.string.settings_sort_by_key),
                context.getString(R.string.settings_sort_by_publishedAt_value)
        );
        String resultSize = sharedPreferences.getString(
                context.getString(R.string.settings_min_result_key),
                context.getString(R.string.settings_min_result_default)
        );
        Uri baseUri = Uri.parse(Constants.NEWS_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(Constants.EVERYTHING)
                .appendQueryParameter("lan","en")
                .appendQueryParameter(Constants.ORDER_BY_PARAM,sortBy)
                .appendQueryParameter(Constants.PAGE_SIZE_PARAM,resultSize)
                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.APIKEY);
        return builder;
    }
    public static String updateQueryUrl(Context context, String searchTerm){
        Uri.Builder builder = makeQuery(context);
        return builder.appendQueryParameter(Constants.QUERY_PARAM,searchTerm).toString();
    }
}
