package com.sire.hefeilife.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sire.hefeilife.R;

import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.item.NormalItemView;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/27
 * Author:Sire
 * Description:
 * ==================================================
 */

public class NavigateTabItem extends NormalItemView {


    public NavigateTabItem(Context context) {
        super(context);
    }

    public NavigateTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigateTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initialize(int drawableRes, int checkedDrawableRes,int pressedDrawableRes, String title) {
        super.initialize(drawableRes, checkedDrawableRes, title);
        StateListDrawable spressedBg = setpressedBg(pressedDrawableRes);
        ImageView viewById = findViewById(me.majiajie.pagerbottomtabstrip.R.id.icon);
        viewById.setBackground(spressedBg);
    }

    private StateListDrawable setpressedBg(int  pressedDrable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable pressed = this.getResources().getDrawable(pressedDrable);
        bg.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
        return bg;
    }

    public static BaseTabItem newItem(int drawable, int checkedDrawable, int pressedDrawable, String text,Context context) {
        NavigateTabItem navigateTabItem = new NavigateTabItem(context);
        navigateTabItem.initialize(drawable, checkedDrawable, pressedDrawable, text);
        navigateTabItem.setTextDefaultColor(Color.GRAY);
        navigateTabItem.setTextCheckedColor(context.getResources().getColor(R.color.colorPrimaryDark));
        return navigateTabItem;
    }
}
