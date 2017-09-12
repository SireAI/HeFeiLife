package com.sire.corelibrary.Networking.downlaod.downloadCore;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:service统一接口数据
 * ==================================================
 */
public interface DownloadWebService {

    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("Range") String start, @Url String url);

}
