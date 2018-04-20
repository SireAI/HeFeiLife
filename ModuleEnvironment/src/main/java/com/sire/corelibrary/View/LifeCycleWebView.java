package com.sire.corelibrary.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public class LifeCycleWebView extends WebView {
    public LifeCycleWebView(Context context) {
        super(context);
    }

    public LifeCycleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LifeCycleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 解决问题：Error: WebView.destroy() called while still attached!
     */
    public void onDestroy() {
        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimers();
    }

    /**
     * 通知内核尝试停止所有处理，如动画和地理位置，但
     * 是不能停止Js，如果想全局停止Js，可以调用pauseTimers()
     * 全局停止Js，调用onResume()恢复
     */
    @Override
    public void onPause() {
        super.onPause();
        pauseTimers();
    }
}
