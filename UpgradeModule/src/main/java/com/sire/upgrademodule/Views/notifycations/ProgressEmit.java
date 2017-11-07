package com.sire.upgrademodule.Views.notifycations;

import android.content.Context;

import com.sire.corelibrary.Utils.CommonUtils;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ProgressEmit {

    private final ProgressNotifycation progressNotifycation;

    public ProgressEmit(Context context, String appName) {
        progressNotifycation = new ProgressNotifycation(context, appName);
    }

    public void notifyProgress(long read, long count, boolean done) {
        ProgressNotifycation.DownloadMessage downloadMessage = new ProgressNotifycation.DownloadMessage();
        downloadMessage.progress = (Double.longBitsToDouble(read) / Double.longBitsToDouble(count) * 100);
        String totalSize = String.valueOf(count / 1024 / 1024);
        String currentSize = String.valueOf(read / 1024 / 1024);
        downloadMessage.leftContent = currentSize + "M/" + totalSize + "M";
        downloadMessage.rightContent = CommonUtils.twoNumberDecimal(downloadMessage.progress) + "%";
        downloadMessage.done = done;
        notifyObservers(downloadMessage);
    }

    public void notifyObservers(ProgressNotifycation.DownloadMessage downloadMessage) {
        if (progressNotifycation != null) {
            progressNotifycation.update(downloadMessage);
        }
    }

    public void setDownloadError() {
        if (progressNotifycation != null) {
            progressNotifycation.setDownloadError();
        }

    }
}
