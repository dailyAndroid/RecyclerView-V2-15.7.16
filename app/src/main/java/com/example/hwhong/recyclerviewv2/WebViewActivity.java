package com.example.hwhong.recyclerviewv2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by hwhong on 7/14/16.
 */
public class WebViewActivity extends FragmentActivity {

    private WebView mWebView;
    private static final String urlTag = "url";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String postUrl = intent.getStringExtra(urlTag);

        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;

        mWebView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebView.loadUrl(postUrl);
        setContentView(mWebView);
    }
}
