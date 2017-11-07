package com.sire.corelibrary.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ToastUtils {
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
