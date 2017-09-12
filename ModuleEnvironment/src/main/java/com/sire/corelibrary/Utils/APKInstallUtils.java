package com.sire.corelibrary.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;

import java.io.File;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/08/28
 * Author:Sire
 * Description:
 * ==================================================
 */
public class APKInstallUtils {
    public static void installApk(Context context,File  file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // 仅需改变这一行
        FileProvider7.setIntentDataAndType(context,
                intent, "application/vnd.android.package-archive", file, true);
        context.startActivity(intent);
    }
}
