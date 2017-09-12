package com.sire.corelibrary.Networking.downlaod.downloadCore;


import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:成功回调处理
 * ==================================================
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     *
     * @param read
     * @param count
     * @param done
     */
    default void update(long read, long count, boolean done) {

    }

    default void onError(Throwable e) {

    }

    default void onComplete(DownloadFileInfor downloadFileInfor) {

    }

    default void onSubscribe() {

    }
}
