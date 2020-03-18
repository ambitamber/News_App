package com.example.newsapp.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.newsapp.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    private static List<News> getFromJson(String allnewsJSON) {
        if (TextUtils.isEmpty(allnewsJSON)) {
            return null;
        }
        List<News> allNewsList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(allnewsJSON);
            Log.println(Log.INFO,LOG_TAG,allnewsJSON);
            JSONArray newsArray = baseJsonResponse.getJSONArray("articles");
            Log.println(Log.INFO,LOG_TAG,String.valueOf(newsArray));

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentnews = newsArray.getJSONObject(i);
                JSONObject source = currentnews.getJSONObject("source");
                String newsTitle = currentnews.getString("title");
                String sourceName = source.getString("name");
                String newsImage = currentnews.getString("urlToImage");
                String newsUpdated = currentnews.getString("publishedAt");
                String newsURL = currentnews.getString("url");
                String descriptionText = currentnews.getString("description");
                News eachItem = new News(newsTitle,sourceName,newsUpdated,newsImage,newsURL,descriptionText);
                allNewsList.add(eachItem);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON data", e);
        }
        return allNewsList;
    }
    public static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<News> fetchBooksData(String requestUrl) throws Exception {
        final int SLEEP_TIME_MILLIS = 2000;
        try {
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "HTTP request: OK");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> listNews = getFromJson(jsonResponse);
        return listNews;
    }
}
