package com.sire.feedmodule.Views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;  
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sire.corelibrary.Utils.APPUtils;
import com.sire.feedmodule.R;

/**  
 * Created by Administrator on 2016/6/16 0016.  
 */  
public class TopToast {

    public static Toast showToast(Context context, String message) {
        //加载Toast布局  
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_componnent_toast, null);
        int fullwidth = APPUtils.getScreenWidth(context);

        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(fullwidth, ViewGroup.LayoutParams.WRAP_CONTENT);;
        //初始化布局控件
        TextView mTextView = toastRoot.findViewById(R.id.message);
        mTextView.setLayoutParams(layoutParams);

        //为控件设置属性
        mTextView.setText(message);  
        //Toast的初始化
        final Toast toastStart = new Toast(context);
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        toastStart.setGravity(Gravity.TOP,0,actionBarHeight);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(toastStart!=null){
                    toastStart.cancel();
                }
            }
        },500);
        return toastStart;
    }  
}