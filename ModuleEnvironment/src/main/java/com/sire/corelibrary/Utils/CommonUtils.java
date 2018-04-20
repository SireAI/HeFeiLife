package com.sire.corelibrary.Utils;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.util.TypedValue;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String text2Html(String text){
        SpannableString spanString= new SpannableString(text);
        String html= Html.toHtml(spanString);
        String html_string= parseUnicodeToStr(html);
        return html_string;
    }

    //unicode转String
    private static String parseUnicodeToStr(String unicodeStr) {
        String regExp = "&#\\d*;";
        Matcher m = Pattern.compile(regExp).matcher(unicodeStr);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String s = m.group(0);
            s = s.replaceAll("(&#)|;", "");
            char c = (char) Integer.parseInt(s);
            m.appendReplacement(sb, Character.toString(c));
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
