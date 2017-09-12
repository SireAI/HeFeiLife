package com.sire.corelibrary.Utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;



/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class UIUtils {

    private UIUtils() {
    }

    /**
     * unit dip to px
     */
    public static int dip2px(Context context,float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
    }

    /**
     * unit px to dip
     */
    public static int px2dip(Context context,float px) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
    }
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    public static int px2sp(Context context,float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static View inflate(int resId, Context context) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    public static View inflate(int resId, ViewGroup container) {
        return LayoutInflater.from(container.getContext()).inflate(resId, container, false);
    }
}
