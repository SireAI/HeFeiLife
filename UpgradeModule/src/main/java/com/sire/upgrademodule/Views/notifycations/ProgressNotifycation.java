package com.sire.upgrademodule.Views.notifycations;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.sire.upgrademodule.BuildConfig;
import com.sire.upgrademodule.R;

import java.util.Observable;
import java.util.Observer;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/15
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ProgressNotifycation  {
    private final NotificationManager manager;
    private final NotificationCompat.Builder builder;

    public ProgressNotifycation(Context context, String appName) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "channel_download";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "下载", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
        builder = new NotificationCompat.Builder(context,channelId);

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("下载" + appName)
                .setContentText("正在下载")
                .setTicker("应用正在下载")
                .setCategory(Notification.CATEGORY_PROGRESS)
                .setOngoing(true)
                .setColor(context.getResources().getColor(R.color.material_cyan_200))
                .setFullScreenIntent(contentIntent,true)
                .setContentIntent(contentIntent)
                .setWhen(SystemClock.currentThreadTimeMillis())
                .setShowWhen(false)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
    }

    public void update( DownloadMessage downloadMessage) {
        if (manager != null && builder != null) {
                if (downloadMessage.done) {
                    builder.setContentText("应用下载完毕").setProgress(0, 0, false);
                    builder .setAutoCancel(true);
                    setNotifycationClear();
                } else {
                    builder.setProgress(100, (int) downloadMessage.progress, false);
                    builder.setContentInfo(downloadMessage.rightContent);
                    builder.setContentText(downloadMessage.leftContent);
                }
            manager.notify(0, builder.build());

        }

    }
    public void setDownloadError(){
        builder.setContentText("应用下载出错");
        builder .setAutoCancel(true);
        manager.notify(0, builder.build());
    }

    public void setNotifycationClear(){
        if(manager!=null){
            manager.cancel(0);
        }
    }

    public static class DownloadMessage {
        public double progress;
        public String leftContent;
        public String rightContent;
        public boolean done;
    }
}
