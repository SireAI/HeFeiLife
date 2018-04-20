package com.sire.corelibrary.Networking.downlaod.subscribers;


import com.sire.corelibrary.Networking.downlaod.RetrofitDownloadManager;
import com.sire.corelibrary.Networking.downlaod.downloadCore.CacheService;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadProgressListener;
import com.sire.corelibrary.Networking.downlaod.downloadCore.ResourceException;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownState;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:
 * 断点下载处理类Subscriber
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * ==================================================
 */
public class DownloadObserver<T> implements DownloadProgressListener, Observer<T> {
    private RetrofitDownloadManager retrofitDownloadManager;
    private CacheService cacheService;
    private DownloadFileInfor downloadFileInfor;
    private DownloadProgressListener downloadProgressListener;


    public DownloadObserver(RetrofitDownloadManager retrofitDownloadManager, CacheService cacheService, DownloadFileInfor downloadFileInfor) {

        this.downloadFileInfor = downloadFileInfor;
        this.cacheService = cacheService;
        this.retrofitDownloadManager = retrofitDownloadManager;
    }


    /**
     * 出错时执行
     * 隐藏ProgressDialog
     *
     * @param error
     */
    @Override
    public void onError(Throwable error) {
        Timber.e("文件下载出错信息：" + error.getMessage());
        if (error instanceof ResourceException) {
            updatePersistent(() -> cacheService.deleteDownloadFileInfor(downloadFileInfor, true));
        } else {
            retrofitDownloadManager.remove(downloadFileInfor);
            downloadFileInfor.setState(DownState.ERROR);
            updatePersistent(() -> cacheService.saveOrUpdate(downloadFileInfor));
        }
        if (downloadProgressListener != null) {
            downloadProgressListener.onError(error);
        }
    }

    public void updatePersistent(Runnable runnable) {
        Observable.just(downloadFileInfor)
                .subscribeOn(Schedulers.io())
                .doOnNext(downloadFileInfor ->
                        runnable.run())
                .subscribe();
    }

    /**
     * 完成所有任务执行
     */
    @Override
    public void onComplete() {
        Timber.i("文件下载成功：" + downloadFileInfor.toString());
        Observable.just(downloadFileInfor)
                .subscribeOn(Schedulers.io())
                .doOnNext(downloadFileInfor -> {
                    retrofitDownloadManager.remove(downloadFileInfor);
                    downloadFileInfor.setState(DownState.FINISH);
                })
                .doAfterNext(downloadFileInfor -> {
                    if(cacheService !=null){
                        cacheService.deleteDownloadFileInfor(downloadFileInfor, false);
                    }
                }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downloadFileInfor -> {
                    if (downloadProgressListener != null) {
                        downloadProgressListener.onComplete(downloadFileInfor);
                    }
                    DownloadObserver.this.clear();
                });
    }

    private void clear() {
        retrofitDownloadManager = null;
        cacheService = null;
        downloadFileInfor = null;
        downloadProgressListener = null;
    }

    /**
     * 任务开始时执行
     *
     * @param disposable
     */
    @Override
    public void onSubscribe(Disposable disposable) {
        if (downloadProgressListener != null) {
            downloadProgressListener.onSubscribe();
        }
        downloadFileInfor.setState(DownState.START);
    }

    /**
     * 当下个任务开始时出发
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
    }

    @Override
    public void update(long read, long count, boolean done) {
        downloadFileInfor.setContentLength(count);
        read+=downloadFileInfor.getTempBrokenPosition();
        downloadFileInfor.setReadStartPonint(read);

        if (downloadProgressListener != null) {
            //not in mainthread
            downloadProgressListener.update(read, count, done);
        }
    }

    public void setDownloadProgressListener(DownloadProgressListener downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
    }
}