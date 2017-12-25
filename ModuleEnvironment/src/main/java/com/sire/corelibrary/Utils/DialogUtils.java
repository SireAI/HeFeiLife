package com.sire.corelibrary.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.sire.corelibrary.R;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/19
 * Author:Sire
 * Description:
 * ==================================================
 */

public class DialogUtils {
    public static void showDialog(Context cotext, String message, DialogInterface.OnClickListener onSureClick){

        AlertDialog.Builder builder = new AlertDialog.Builder(cotext, R.style.dialog_marterial);
        AlertDialog alertDialog = builder.setTitle("温馨提示").setMessage(message).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", onSureClick).create();
        alertDialog.show();
    }
    public static void showDialog(Context cotext, String message,DialogInterface.OnClickListener onDismiss, DialogInterface.OnClickListener onSureClick){

        AlertDialog.Builder builder = new AlertDialog.Builder(cotext, R.style.dialog_marterial);
        AlertDialog alertDialog = builder.setTitle("温馨提示").setMessage(message).setNegativeButton("取消", onDismiss).setPositiveButton("确定", onSureClick).create();
        alertDialog.show();
    }

    public static void showDialog(Context cotext,String positiveButton,String negativeButton, String message,DialogInterface.OnClickListener onDismiss, DialogInterface.OnClickListener onSureClick){

        AlertDialog.Builder builder = new AlertDialog.Builder(cotext, R.style.dialog_marterial);
        AlertDialog alertDialog = builder.setTitle("温馨提示").setMessage(message).setNegativeButton(negativeButton, onDismiss).setPositiveButton(positiveButton, onSureClick).create();
        alertDialog.show();
    }
}
