package com.tuochebang.service.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.FileChooserParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.framework.app.component.widget.DataLoadingView;
import com.tuochebang.service.R;
import com.tuochebang.service.base.BaseActivity;

public class WebActivity extends BaseActivity {
    public static final String FALG_FROM = "from_activity";
    public static final String FALG_KEY = "key_activity";
    public static final String FLAG_SHOW_DIAL_BTN = "show_dial_btn";
    public static final String FLAG_TITLE = "title";
    public static final String FLAG_URL = "url";
    private static final String TAG = "WebActivity";
    private String content;
    private String image;
    private String mActivityKey;
    private DataLoadingView mDataLoadingView;
    private String mFrom;
    private boolean mShowDialBtn;
    private String mTitle;
    private Toolbar mToolBar;
    private String mUrl;
    private WebView mWebView;
    private String targetUrl;
    private String title;

    /* renamed from: com.tuochebang.service.ui.WebActivity$1 */
    class C14671 extends WebViewClient {
        C14671() {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebActivity.this.mDataLoadingView.showDataLoading();
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebActivity.this.mDataLoadingView.showDataLoadSuccess();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebActivity.this.mWebView.loadUrl(url);
            return true;
        }
    }

    /* renamed from: com.tuochebang.service.ui.WebActivity$2 */
    class C14682 implements OnClickListener {
        C14682() {
        }

        public void onClick(View v) {
            WebActivity.this.finish();
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @TargetApi(21)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            Intent intent = fileChooserParams.createIntent();
            return true;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getIntentExtras();
        initActionBar();
        initViews();
    }

    private void getIntentExtras() {
        this.mUrl = getIntent().getStringExtra(FLAG_URL);
        this.mFrom = getIntent().getStringExtra(FALG_FROM);
        this.mUrl = new StringBuilder(this.mUrl).toString();
        this.mTitle = getIntent().getStringExtra("title");
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initViews() {
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loading_view);
        this.mWebView = (WebView) findViewById(R.id.web_view);
        this.mWebView.clearCache(true);
        WebSettings wSet = this.mWebView.getSettings();
        wSet.setJavaScriptEnabled(true);
        wSet.setDomStorageEnabled(true);
        this.mWebView.setWebViewClient(new C14671());
        this.mWebView.setWebChromeClient(new MyWebChromeClient());
        this.mWebView.loadUrl(this.mUrl);
    }

    private void initActionBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C14682());
        this.mToolBar.setTitle(this.mTitle);
    }

    public void onBackPressed() {
        if (this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
