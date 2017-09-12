package com.sire.corelibrary.Networking.downlaod.downloadCore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

import java.io.IOException;
import java.util.List;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:断点续传
 * ==================================================
 */

 public interface CacheService {

     void saveOrUpdate(DownloadFileInfor info);


     void save(DownloadFileInfor info);

     void update(DownloadFileInfor info);

     void deleteDownloadFileInfor(DownloadFileInfor info,boolean deleteTargetFile);


     DownloadFileInfor findUnfinishedDownloadingBy(long Id) ;

     List<DownloadFileInfor> queryDownAll();
}
