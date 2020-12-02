package com.dayquote.quotefortheday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dayquote.quotefortheday.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.wessam.library.LayoutImage;
import com.wessam.library.NetworkChecker;
import com.wessam.library.NoInternetLayout;

import java.util.List;
import java.util.Objects;

public class WikiActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);

        //przesłane dane z mainactivity
        String quoteWiki = getIntent().getStringExtra("quoteWiki");
        String quoteAuthor=getIntent().getStringExtra("quoteAuthor");

        Objects.requireNonNull(getSupportActionBar()).setTitle(quoteAuthor);
        if(NetworkChecker.isNetworkConnected(WikiActivity.this)) {
            webView = findViewById(R.id.web_view);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(quoteWiki);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }else{

            //check internet connection
            new NoInternetLayout.Builder(WikiActivity.this, R.layout.activity_wiki)
                    .animate()
                    .mainTitle("No internet connection")
                    .secondaryText("Please check your internet connection and try again")
                    .buttonText("RETRY")
                    .setImage(LayoutImage.SHELL);

        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}