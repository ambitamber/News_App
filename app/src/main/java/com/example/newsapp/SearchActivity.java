package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private static final String TAG = "MainActivity";

    private CustomAdapter mAdapter;
    private TextView mEmptyStateTextView;//TextView that is displayed when the list is empty
    private View progressBar;
    private String mUrlRequestNEWSAPI ="";
    boolean isConnected;
    private EditText mSearchViewField;
    public ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        checkConnection(cm);

        listView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.emptyText);
        progressBar = findViewById(R.id.progress_circular);
        mSearchViewField = findViewById(R.id.edit_query);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter= new CustomAdapter(this,new ArrayList<News>());
        listView.setAdapter(mAdapter);

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager.getInstance(this).initLoader(NEWS_LOADER_ID,null, this);
        } else {
            // Progress bar mapping
            progressBar.setVisibility(GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText("No Internet Connection");
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
                Intent intent = new Intent(SearchActivity.this, WebViewer.class);
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
                checkConnection(cm);
                if (isConnected) {
                    if (TextUtils.isEmpty(mSearchViewField.getText())){
                        mEmptyStateTextView.setText("No Search Value");
                    }else {
                        restartLoader();
                        UrlQueryParameter.updateQueryUrl(getBaseContext(),mSearchViewField.getText().toString());
                    }
                }else{
                    // Clear the adapter of previous book data
                    mAdapter.clear();
                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    // ...and display message: "No internet connection."
                    mEmptyStateTextView.setText(R.string.no_internet);
                }
            }
        });
        mSearchViewField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    checkConnection(cm);
                    if (isConnected) {
                        if (TextUtils.isEmpty(mSearchViewField.getText())){
                            mEmptyStateTextView.setText("No Search Value");
                        }else {
                            restartLoader();
                            UrlQueryParameter.updateQueryUrl(getBaseContext(),mSearchViewField.getText().toString());
                        }
                    }else{
                        // Clear the adapter of previous book data
                        mAdapter.clear();
                        // Set mEmptyStateTextView visible
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        // ...and display message: "No internet connection."
                        mEmptyStateTextView.setText(R.string.no_internet);
                    }
                    InputMethodManager im = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert im != null;
                    im.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void checkConnection(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        LoaderManager.getInstance(SearchActivity.this).restartLoader(NEWS_LOADER_ID, null, SearchActivity.this);
        swipeRefreshLayout.setRefreshing(false);
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        UrlQueryParameter.updateQueryUrl(getBaseContext(),mSearchViewField.getText().toString());
        mUrlRequestNEWSAPI = UrlQueryParameter.updateQueryUrl(getBaseContext(),mSearchViewField.getText().toString());
        Log.e(TAG,mUrlRequestNEWSAPI);
        return new CustomLoader(this,mUrlRequestNEWSAPI);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        // Hide loading indicator because the data has been loaded
        progressBar.setVisibility(GONE);
        // Set empty state text to display "No Books found."
        mEmptyStateTextView.setText("No News Found");
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}