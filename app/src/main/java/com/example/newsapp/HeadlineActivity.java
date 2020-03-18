package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class HeadlineActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int LOADER_ID = 2;
    ListView listView;
    CustomAdapter mAdapter;
    private View progressBar;
    boolean isConnected;
    TextView mTextView;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headline);

        //TextView that is displayed when the list is empty
        mTextView = findViewById(R.id.emptyText);
        progressBar = findViewById(R.id.progress_bar);
        listView = findViewById(R.id.headlineList);
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Top Headlines");
        mAdapter = new CustomAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isConnected = true;
            androidx.loader.app.LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
        } else {
            isConnected = false;
            progressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(R.string.no_internet);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                assert currentNews != null;
                final Uri newsUrl = Uri.parse(currentNews.getNewsURL());
                String imageUri = currentNews.getImageURL();
                String titlenews = currentNews.getNewsTitle();
                String sourceName = currentNews.getNewsSection();
                Intent intent = new Intent(HeadlineActivity.this, WebViewer.class);
                intent.putExtra("siteimage",imageUri);
                intent.putExtra("siteaddres",newsUrl.toString());
                intent.putExtra("sitetitle",titlenews);
                intent.putExtra("sitesource",sourceName);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restartLoader();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.headlinemenu, menu);
        return true;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        String url = "http://newsapi.org/v2/top-headlines?country=us&sortby=publishedAt&apiKey=5b5ab3f933184289a68cae008cd352d1";
        return new CustomLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            mTextView.setVisibility(GONE);
            // Hide loading indicator because the data has been loaded
            progressBar.setVisibility(GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_Button:
                Intent intent = new Intent(HeadlineActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutButton1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restartLoader() {
        if (isConnected){
            mTextView.setVisibility(GONE);
            progressBar.setVisibility(View.VISIBLE);
            LoaderManager.getInstance(HeadlineActivity.this).restartLoader(LOADER_ID, null, HeadlineActivity.this);
            swipeRefreshLayout.setRefreshing(false);
        }else {
            mTextView.setText(R.string.no_internet);
        }
    }
}
