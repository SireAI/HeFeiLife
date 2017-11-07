package com.sire.usermodule.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(cotext);
        final AlertDialog alertDialog = builder.setTitle("注意").setMessage(message).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("确定", onSureClick).create();
        alertDialog.show();
    }
}
