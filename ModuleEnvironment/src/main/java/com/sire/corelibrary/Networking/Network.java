package com.sire.corelibrary.Networking;


import android.content.Context;

import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/19
 * Author:sire
 * Description:
 * ==================================================
 */
public class Network {
    public static final String LOGIN_TOKEN = "token";
    /**
     * 业务正确码
     */
    public static int OK = 200;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Map<String, Object> apiCache = new HashMap<>();

    public static <T> T getNetApi(Class<T> apiInterface) {
        String apiName = apiInterface.getSimpleName();
        Object api = apiCache.get(apiName);
        if (api != null) {
            return (T) api;
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(WebUrl.getHostUrl())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            T newApi = retrofit.create(apiInterface);
            apiCache.put(apiName, newApi);
            return newApi;
        }
    }

    public static OkHttpClient genericClient(Context context) {

        OkHttpClient httpClient = new OkHttpClient.Builder()

                .addInterceptor(chain -> {

                    Request request = chain.request().newBuilder()
                            .addHeader("token", SPUtils.getValueString(context, LOGIN_TOKEN))
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Cache-Control", "no-cache")
                            .addHeader("App-Version-Code", APPUtils.getVersionCode(context)+"")
                            .addHeader("App-Version-Name", APPUtils.getVersionName(context))
                            .build();
                    return chain.proceed(request);

                }).build();


        return httpClient;

    }
    public static RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return body.contentType();
            }

            @Override public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
//                gzipSink.close();
            }
        };

    }
}
