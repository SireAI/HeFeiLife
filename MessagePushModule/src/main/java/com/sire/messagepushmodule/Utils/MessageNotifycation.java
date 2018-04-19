package com.sire.messagepushmodule.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.sire.corelibrary.Utils.SystemUtils;
import com.sire.messagepushmodule.R;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/02/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class MessageNotifycation {

    public static final String COM_SIRE_NOTIFYCATION_ENTRANCE = "com.sire.notifycation.entrance";
    private static MessageNotifycation mInstance;
    private final NotificationManager manager;
    private String channelId;
    private int notifycationId;
    private final int COUNT_LIMIT=8;

    private MessageNotifycation(Context context) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        channelId = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "channel_message";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "消息", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public static MessageNotifycation getInstance(Context context) {
        synchronized (COM_SIRE_NOTIFYCATION_ENTRANCE) {
            if (mInstance == null) {
                mInstance = new MessageNotifycation(context);
            }
            return mInstance;
        }
    }

    public void sendNotifycationMessage(Context context, String messageTitle, String messageContent) {
        setNotifycationID();
        boolean background = SystemUtils.isBackground(context);
        Intent intent = new Intent();
        //应用如果处于后台，那么就清空原有状态重启，如果处于前台则不处理
        if(background){
            intent = new Intent();
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setAction(COM_SIRE_NOTIFYCATION_ENTRANCE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, notifycationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle(messageTitle)
                .setContentText(messageContent)
                .setTicker(messageContent)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(DEFAULT_ALL)
                .setColor(context.getResources().getColor(R.color.material_cyan_200))
                .setFullScreenIntent(contentIntent,background)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
        manager.notify(notifycationId, builder.build());

    }

    private void setNotifycationID() {
        notifycationId++;
        if(notifycationId >COUNT_LIMIT){
            notifycationId = 1;
        }
    }
}
