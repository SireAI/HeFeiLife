package com.sire.corelibrary.Networking.NetConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.sire.corelibrary.Event.NetChangeEvent;

import org.greenrobot.eventbus.EventBus;



/**
 * All Right Reserved
 * Date:2015/10/26
 * Author:sire
 * Description:manage net
 */
public class NetStateReceiver extends BroadcastReceiver {
    public static void registerSelf(Context context) {
        NetStateReceiver netHandler = new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.getApplicationContext().registerReceiver(netHandler, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) || intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            broadcastNetConnected();
        }
    }

    private void broadcastNetConnected() {
        EventBus.getDefault().post(new NetChangeEvent());
    }

}
