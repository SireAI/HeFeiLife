package com.sire.corelibrary.Networking;


import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/19
 * Author:sire
 * Description:
 * ==================================================
 */
public class Network {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Map<String,Object> apiCache = new HashMap<>();
    /**
     * 业务正确码
     */
    public static int OK = 200;
    public static <T> T getNetApi(Class<T> apiInterface) {
        String apiName = apiInterface.getSimpleName();
        Object api = apiCache.get(apiName);
        if(api!=null){
            return (T)api;
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(WebUrl.getHostUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            T newApi = retrofit.create(apiInterface);
            apiCache.put(apiName,newApi);
            return newApi;
        }
    }
}
