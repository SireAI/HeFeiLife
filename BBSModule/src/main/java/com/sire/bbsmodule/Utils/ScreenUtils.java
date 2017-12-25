package com.sire.bbsmodule.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sire.corelibrary.Utils.CommonUtils;
import com.sire.corelibrary.Utils.SPUtils;

import static android.R.attr.uiOptions;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/30
 * Author:sire
 * Description:
 * ==================================================
 */
public class ScreenUtils {
    public static int getKeyboardHeight(Activity paramActivity) {

        int height = ScreenUtils.getScreenHeight(paramActivity) - ScreenUtils.getStatusBarHeight(paramActivity)
                - ScreenUtils.getAppHeight(paramActivity);
        if (height == 0) {
            height = SPUtils.getValueInteger(paramActivity,"KeyboardHeight", 787);//787为默认软键盘高度 基本差不离
        }else{
            SPUtils.saveKeyValueInteger(paramActivity,"KeyboardHeight", height);
        }
        return height;
    }
    /**屏幕分辨率高**/
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }
    public static int getScreenWidth(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
    /**statusBar高度**/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }
    /**可见屏幕高度**/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }
    /**关闭键盘**/
    public static void hideSoftInput(View paramEditText) {
        ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }
    // below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return ScreenUtils.getScreenHeight(paramActivity) - ScreenUtils.getStatusBarHeight(paramActivity)
                - ScreenUtils.getActionBarHeight(paramActivity) - ScreenUtils.getKeyboardHeight(paramActivity);
    }
    /**获取actiobar高度**/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return CommonUtils.dip2px(paramActivity,56);
        }
        int[] attrs = new int[] { android.R.attr.actionBarSize };
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, CommonUtils.dip2px(paramActivity,56));
    }

    /**键盘是否在显示**/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = ScreenUtils.getScreenHeight(paramActivity) - ScreenUtils.getStatusBarHeight(paramActivity)
                - ScreenUtils.getAppHeight(paramActivity);
        return height != 0;
    }
    /**显示键盘**/
    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) paramEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(paramEditText, InputMethodManager.SHOW_FORCED);
            }
        });
    }
    public static void fullScreen(Activity activity) {

        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;

        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("TEST", "Turning immersive mode mode off. ");
        } else {
            Log.i("TEST", "Turning immersive mode mode on.");
        }
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }


}