package com.sire.corelibrary.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;



/**
 *  All Right Reserved
 *  Date:2015/10/26
 *  Author:sire
 *  Description: check wheather the net of the phone is good
 */

public class NetUtil {
	private NetUtil() {
	}

	/**
	 * check the base state
	 * @return
	 */
	public static boolean isNetConnect(Context context) {
		ConnectivityManager manager=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
			if(manager==null)return false;
			Network[] infos = manager.getAllNetworks();
			if(infos == null)return false;
			if (infos != null) {
				for (Network ni : infos) {
					NetworkInfo networkInfo = manager.getNetworkInfo(ni);
					if(networkInfo !=null&& networkInfo.isConnected()){
						return true;
					}
				}
			}
			return false;
		}else {
			if (manager != null) {
				NetworkInfo[] infos = manager.getAllNetworkInfo();
				if (infos != null) {
					for (NetworkInfo ni : infos) {
						if (ni.isConnected()) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
