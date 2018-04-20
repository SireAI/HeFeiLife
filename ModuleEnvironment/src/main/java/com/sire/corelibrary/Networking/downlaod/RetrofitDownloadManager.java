package com.sire.corelibrary.Networking.downlaod;


import android.content.Context;

import com.sire.corelibrary.Networking.WebUrl;
import com.sire.corelibrary.Networking.downlaod.downloadCore.CacheService;
import com.sire.corelibrary.Networking.downlaod.downloadCore.CacheServiceImpl;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadInterceptor;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadProgressListener;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadWebService;
import com.sire.corelibrary.Networking.downlaod.downloadCore.ResourceException;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;
import com.sire.corelibrary.Networking.downlaod.subscribers.DownloadObserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

import static com.sire.corelibrary.Networking.downlaod.DownloadCacheUtil.getBasUrl;
import static com.sire.corelibrary.Networking.downlaod.DownloadCacheUtil.writeFile;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:http下载处理类
 * ==================================================
 */
public class RetrofitDownloadManager {
    public static final int DOWNLOAD_BY_ONCE = 0;
    /*单利对象*/
    private volatile static RetrofitDownloadManager INSTANCE;
    /*记录下载数据*/
    private Set<DownloadFileInfor> downloadingFileInfors;
    /*回调sub队列*/
    private HashMap<String, DownloadObserver> subMap;
    /*数据库类*/
    private CacheService cacheService;

    private RetrofitDownloadManager(Context context) {
        downloadingFileInfors = new HashSet<>();
        subMap = new HashMap<>();
        cacheService = new CacheServiceImpl(context);
    }


    /**
     * 获取单例
     *
     * @return
     */
    public static RetrofitDownloadManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RetrofitDownloadManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitDownloadManager(context);
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 必填参数：savePath，readLength，url
     *
     * @param info
     */
    public void downloadFile(DownloadFileInfor info, DownloadProgressListener downloadProgressListener) {

        if (info == null || subMap.get(info.getUrl()) != null) return;
        DownloadObserver downloadObserver = new DownloadObserver(this, cacheService, info);
        if (downloadProgressListener != null) {
            downloadObserver.setDownloadProgressListener(downloadProgressListener);
        }
        subMap.put(info.getUrl(), downloadObserver);
        DownloadInterceptor interceptor = new DownloadInterceptor(downloadObserver);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(info.getConnectonTime(), TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getBasUrl(info.getUrl())+"footprint/")
                .build();
        DownloadWebService downloadWebService = retrofit.create(DownloadWebService.class);

        downloadingFileInfors.add(info);
        Timber.i("文件下载初始信息："+info.toString());
        download(downloadWebService, info, downloadObserver);
    }

    public void download(DownloadWebService downloadWebService, DownloadFileInfor info, DownloadObserver downloadObserver) {
        Observable.just(info)
                .map(downloadFileInfor -> {
                    DownloadFileInfor unfinishedDownloading = cacheService.findUnfinishedDownloadingBy(downloadFileInfor.getId());
                    if (unfinishedDownloading != null) {
                        downloadFileInfor.copy(unfinishedDownloading);
                        downloadFileInfor.setTempBrokenPosition(downloadFileInfor.getReadStartPonint());
                        Timber.i("文件下载断点信息："+info.toString());
                    }

                    return downloadFileInfor;
                })
                .flatMap(downloadFileInfor -> {
                    String range = downloadFileInfor.getReadStartPonint() + "-";
                    return downloadWebService.download("byte=" + range, downloadFileInfor.getUrl());
                })
                .map(responseBody -> {
                    try {
                        writeFile(responseBody, new File(info.getSavePath()), info);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    } catch (IllegalArgumentException e){
                        //资源异常
                        String result  = responseBody.string();
                        throw new ResourceException(result);
                    }
                    return info;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadObserver);


    }


    /**
     * 停止下载
     */
//    public void stopDown(DownloadFileInfor info){
//        if(info==null)return;
//        info.setState(DownState.STOP);
//        info.getListener().onStop();
//        if(subMap.containsKey(info.getUrl())) {
//            DownloadObserver subscriber=subMap.get(info.getUrl());
////            subscriber.unsubscribe();
//            subMap.remove(info.getUrl());
//        }
//        /*保存数据库信息和本地文件*/
//        cacheService.save(info);
//    }


    /**
     * 暂停下载
     * @param info
     */
//    public void pause(DownloadFileInfor info){
//        if(info==null)return;
//        info.setState(DownState.PAUSE);
//        info.getListener().onPuase();
//        if(subMap.containsKey(info.getUrl())){
//            DownloadObserver subscriber=subMap.get(info.getUrl());
//            subscriber.unsubscribe();
//            subMap.remove(info.getUrl());
//        }
//        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
//        cacheService.update(info);
//    }

    /**
     * 停止全部下载
     */
//    public void stopAllDown(){
//        for (DownloadFileInfor downloadFileInfor : downloadingFileInfors) {
//            stopDown(downloadFileInfor);
//        }
//        subMap.clear();
//        downloadingFileInfors.clear();
//    }

    /**
     * 暂停全部下载
     */
//    public void pauseAll(){
//        for (DownloadFileInfor downloadFileInfor : downloadingFileInfors) {
//            pause(downloadFileInfor);
//        }
//        subMap.clear();
//        downloadingFileInfors.clear();
//    }


    /**
     * 返回全部正在下载的数据
     *
     * @return
     */
    public Set<DownloadFileInfor> getDownloadWebServices() {
        return downloadingFileInfors;
    }

    /**
     * 移除下载数据
     *
     * @param info
     */
    public void remove(DownloadFileInfor info) {
        subMap.remove(info.getUrl());
        downloadingFileInfors.remove(info);
    }


}
