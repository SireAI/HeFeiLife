package com.sire.corelibrary.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.os.SystemClock;
import android.telephony.TelephonyManager;

import com.sire.corelibrary.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * ==================================================
 * All Right Reserved yapingguo
 * Date:2015/9/21
 * Author:sire
 * Description:
 * ==================================================
 */

public class SystemUtils {


    /**
     * double click,change the number to define click time
     */
    private static long[] mHits = new long[2];

    /**
     * 取得当前sim手机卡的imsi
     */
    public static String getIMSI(Context context) {
        if (null == context) {
            return null;
        }
        String imsi = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return imsi;
    }

    /**
     * 获取当前设备的MAC地址
     */
    public static String getMacAddress(Context context) {

        String mac = null;
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return mac;
    }

    /**
     * 获得设备ip地址
     */
    public static String getLocalAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            LogUtils.e(e.getMessage());
        }
        return null;
    }

    public static boolean isFastClick(long passTime) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - (passTime <= 500 ? 500 : passTime))) {
            return true;
        }
        return false;
    }


    public static void createShortCut(Context context, Class launcherActivity, String appName) {
        boolean isAddShortCut = SPUtils.getValueBoolen(context, "short_cut_added");
        if (isAddShortCut) {
            return;
        }
        SPUtils.getValueBoolen(context, "short_cut_added");
        //创建快捷方式的Intent
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        //快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, R.drawable.logo);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 设置关联程序
        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(context, launcherActivity);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        //发送广播
        context.sendBroadcast(shortcutintent);
    }


    public static void dial(Activity activity, String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }
}
