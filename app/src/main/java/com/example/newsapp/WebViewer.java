package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.view.View.GONE;

public class WebViewer extends AppCompatActivity {

    private static final String LOG_TAG = "WebViewer";
    WebView webViewer;
    ProgressBar progressBar;
    TextView emptyView,appBarText,toolBarTitle;
    Toolbar toolbar;
    AppBarLayout appBarLayout;
    Menu menu;
    ImageView appBarImageView;
    private Uri addressData = null;
    String imageData = null;
    String titleData = null;
    String sourceData = null;
    FloatingActionButton floatingActionButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);

        webViewer = findViewById(R.id.webView1);
        progressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        appBarLayout = findViewById(R.id.appBar);
        appBarImageView = findViewById(R.id.appBarImage);
        appBarText = findViewById(R.id.appBarText);
        floatingActionButton = findViewById(R.id.fab);
        toolBarTitle = findViewById(R.id.toolbarTitle);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle !=null){
            addressData = Uri.parse(bundle.getString("siteaddres"));
            imageData = bundle.getString("siteimage");
            titleData = bundle.getString("sitetitle");
            sourceData = bundle.getString("sitesource");
        }
        Picasso.get().load(imageData).into(appBarImageView);
        appBarText.setText(titleData);
        toolBarTitle.setText(sourceData);

        webViewer.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        webViewer.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, titleData + " "+ addressData.toString());
                intent.setType("text/html");
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
            }
        });

        emptyView = findViewById(R.id.emptyText);
        if (isConnected()){
            webViewer.loadUrl(String.valueOf(addressData));
            emptyView.setVisibility(GONE);
        }else {
            emptyView.setText(R.string.no_internet);
            progressBar.setVisibility(GONE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    boolean isConnected() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu,menu);
        hide_show_edit();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                return true;
            case R.id.share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, titleData + " "+ addressData.toString());
                intent.setType("text/html");
                Intent shareIntent = Intent.createChooser(intent, null);
                startActivity(shareIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void hide_show_edit(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                MenuItem menuItem = menu.findItem(R.id.share);
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()){
                    menuItem.setVisible(true);
                }else if (verticalOffset==0){
                    menuItem.setVisible(false);
                }
            }
        });
    }
}
