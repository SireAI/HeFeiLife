package com.sire.bbsmodule.Controller;

import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.sire.bbsmodule.R;
import com.sire.corelibrary.Controller.SireController;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public class Html5Controller extends SireController {

    private WebView wvPage;
    private Toolbar toolbar;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_html5);
        wvPage = findViewById(R.id.wv_page);
        toolbar = findViewById(R.id.toolbar);
        pb = findViewById(R.id.pb);
        initSetting();
    }

    private void initSetting() {
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar.setTitle(title);
            }

            /**
             * webview打开文件，这个方法只支持5.0以上
             * @param webView
             * @param filePathCallback
             * @param fileChooserParams
             * @return
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完毕进度条消失
                    pb.setVisibility(View.GONE);
                } else {
                    //更新进度
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        };
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(!wvPage.getSettings().getLoadsImagesAutomatically()) {
                    wvPage.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            /**
             * 网页加载出错的几种自定义处理方式接口
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        };
        WebSettings settings = wvPage.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        //图片后加载以提高加载速度
        if(Build.VERSION.SDK_INT >= 19) {
            wvPage.getSettings().setLoadsImagesAutomatically(true);
        } else {
            wvPage.getSettings().setLoadsImagesAutomatically(false);
        }
        wvPage.setWebChromeClient(webChromeClient);
        wvPage.setWebViewClient(webViewClient);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以,同时支持https 与http的混合
            settings.setMixedContentMode(settings.getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }
}
