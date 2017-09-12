package com.sire.corelibrary.Networking;

import com.sire.corelibrary.Utils.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/08/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class URLSign {
    public static final String TIME_STAMP = "timeStamp";
    public static final String SIGN = "sign";
    private final Map<String, String> params;
    private final String url;

    public URLSign(String url, LinkedHashMap<String, String> params) {
        this.url = url;
        if(params==null){
            throw new RuntimeException("params can't be null!");
        }
        this.params = params;
    }


    private String signParams(long timeStamp) {
        ArrayList<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder(url);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            builder.append(key + "=" + params.get(key));
        }
        builder.append(TIME_STAMP + "=" + timeStamp);
        String md5Value = MD5Utils.MD5(builder.toString());
        return md5Value;
    }

    private long getCurrentTime() {
        long deltaTimeBetweenDeviceAndServer = 0;
        long deviceTime = System.currentTimeMillis();
        long serverTime = deviceTime + deltaTimeBetweenDeviceAndServer;
        return serverTime;
    }

    public void sign() {
        long currentTime = getCurrentTime();
        String sign = signParams(currentTime);
        LinkedHashMap<String, String> signValues = new LinkedHashMap<>();
        signValues.put(TIME_STAMP, currentTime + "");
        signValues.put(SIGN, sign);
        params.putAll(signValues);
    }


}
