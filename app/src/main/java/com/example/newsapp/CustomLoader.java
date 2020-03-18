package com.example.newsapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.newsapp.utils.QueryUtils;

import java.util.List;

public class CustomLoader extends AsyncTaskLoader<List<News>> {
    /** Tag for log messages */
    private static final String LOG_TAG = CustomLoader.class.getName();
    /** QueryUtils URL */
    private String mUrl;
    public CustomLoader(Context context, String url){
        super(context);
        Log.i(LOG_TAG,"CustomLoader is called");
        mUrl = url;
    }
    protected void onStartLoading(){
        Log.i(LOG_TAG,"onStartLoading() is called");
        forceLoad();
    }
    @Nullable
    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG,"loadInBackground() is called");
        if (mUrl==null){
            return null;
        }
        List<News> list = null;
        try {
            list = QueryUtils.fetchBooksData(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
