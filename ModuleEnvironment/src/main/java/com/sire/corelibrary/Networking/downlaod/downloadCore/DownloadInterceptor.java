package com.sire.corelibrary.Networking.downlaod.downloadCore;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:自定义responsebody载入请求中
 * ==================================================
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(),originalResponse.header("file-size"), listener))
                .build();
    }
}
