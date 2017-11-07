package com.sire.corelibrary.Utils;

import android.content.Context;
import android.util.TypedValue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CommonUtils {
    public static String twoNumberDecimal(Double number){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String twoDecimalNumber = decimalFormat.format(number);
        return twoDecimalNumber;
    }
    public static String formatDate(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }


    /**
     * unit dip to px
     */
    public static int dip2px(Context context, float dip) {
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

    public static int px2sp(Context context,float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
