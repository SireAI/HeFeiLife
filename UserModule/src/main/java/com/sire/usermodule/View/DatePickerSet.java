package com.sire.usermodule.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/23
 * Author:Sire
 * Description:
 * ==================================================
 */

public class DatePickerSet {

    public static void setDatePicker(Activity context) throws NoSuchFieldException, IllegalAccessException {
        LinkedList<NumberPicker> pickers = new LinkedList<>();
        Class<? extends DatePicker> c =DatePicker.class;
        Field fd = null, fm = null, fy = null;
        View daySpinner = null;
        View monthSpinner= null;
        View yearSpinner = null;
        // 系统版本大于5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //获取资源的id
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
            if (daySpinnerId != 0 && monthSpinnerId!=0 && yearSpinnerId!=0) {
                daySpinner = context.findViewById(daySpinnerId);
                monthSpinner = context.findViewById(monthSpinnerId);
                yearSpinner = context.findViewById(yearSpinnerId);
            }
        } else {
            fd = c.getDeclaredField("mDayPicker");
            fm = c.getDeclaredField("mMonthPicker");
            fy = c.getDeclaredField("mYearPicker");
            if(fd!=null && fm!=null &&fy!=null){
                // 对字段获取设置权限
                fd.setAccessible(true);
                fm.setAccessible(true);
                fy.setAccessible(true);
                // 得到对应的控件
                daySpinner = (View) fd.get(context);
                monthSpinner= (View) fm.get(context);
                yearSpinner= (View) fy.get(context);
            }
        }
        if (daySpinner != null && monthSpinner != null && yearSpinner != null) {
            if(daySpinner instanceof NumberPicker && monthSpinner instanceof NumberPicker && yearSpinner instanceof NumberPicker ){
                pickers.add((NumberPicker) daySpinner);
                pickers.add((NumberPicker) monthSpinner);
                pickers.add((NumberPicker) yearSpinner);
            }
        }

      batchSet(pickers);
    }

    private static void batchSet(LinkedList<NumberPicker> pickers) throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < pickers.size(); i++) {

            setNumberPickerDivider(pickers.get(i));
        }
    }

    /**
     * 设置picker之间的间距
     */
    private static void setPickerMargin(NumberPicker picker) {
        LinearLayout.LayoutParams p= (LinearLayout.LayoutParams) picker.getLayoutParams();
        p.setMargins(0,0,0,0);
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN_MR1){
            p.setMarginStart(0);
            p.setMarginEnd(0);
        }
    }

    /**
     * 设置picker分割线的颜色
     */
    private static void setDividerColor(NumberPicker picker) throws NoSuchFieldException, IllegalAccessException {
        Field field=NumberPicker.class.getDeclaredField("mSelectionDivider");
        if(field!=null){
            field.setAccessible(true);
            field.set(picker,new ColorDrawable(Color.RED));
        }
    }

    /**
     * 设置picker分割线的宽度
     */
    private static void setNumberPickerDivider(NumberPicker picker) throws IllegalAccessException {
        Field[] fields=NumberPicker.class.getDeclaredFields();
        for(Field f:fields){
            if(f.getName().equals("mSelectionDividerHeight")){
                f.setAccessible(true);
                f.set(picker,0);
                break;
            }
        }
    }
    /**
     * 设置文字大小
     */
    private static void setNumberPickerTextSize(NumberPicker picker) throws IllegalAccessException {
        EditText editText = findEditText(picker);

        editText.setFocusable(false);
        editText.setGravity(Gravity.CENTER);
        editText.setTextSize(38);

    }

    public static EditText findEditText(NumberPicker np) {

        if (np != null) {
            for (int i = 0; i < np.getChildCount(); i++) {
                View child = np.getChildAt(i);
                if (child instanceof EditText) {
                    return (EditText) child;
                }
            }
        }
        return null;
    }

}
