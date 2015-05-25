package com.posagent.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.epalmpay.agentPhone.R;

/**
 * Created by Leo on 2015/2/11.
 */
public class Webview extends BaseActivity {

    private String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        new TitleMenuUtil(context, "网页浏览").show();

        url = getIntent().getStringExtra("url");

        WebView webview = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // localstorage 生效
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        webview.loadUrl(url);

        //加这个让 alert 有效
        webview.setWebChromeClient(new WebChromeClient());
        webview.addJavascriptInterface(this, "YYDAndroidApp");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
