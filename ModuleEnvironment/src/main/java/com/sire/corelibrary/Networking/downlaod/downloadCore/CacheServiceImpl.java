package com.sire.corelibrary.Networking.downlaod.downloadCore;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sire.corelibrary.DI.Environment.EnvironmentDB;
import com.sire.corelibrary.Networking.downlaod.DB.Dao.DownloadInforDao;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

import java.io.File;
import java.util.List;

import timber.log.Timber;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/04
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CacheServiceImpl implements CacheService{

    private final DownloadInforDao downloadInforDao;
    public CacheServiceImpl(Context context) {
        downloadInforDao = Room.databaseBuilder(context, EnvironmentDB.class, EnvironmentDB.ENVIRONMENT).build().downloadInforDao();
    }

    @Override
    public void saveOrUpdate(DownloadFileInfor info) {
        Timber.i("存储下载文件信息："+info);
        downloadInforDao.saveOrUpdate(info);
    }

    @Override
    public void save(DownloadFileInfor info) {
        downloadInforDao.save(info);
    }

    @Override
    public void update(DownloadFileInfor info) {
        downloadInforDao.update(info);
    }

    @Override
    public void deleteDownloadFileInfor(DownloadFileInfor info,boolean deleteTargetFile)  {
        downloadInforDao.deleteDowninfo(info);
        String message = "删除下载文件信息";
        if(deleteTargetFile){
            File targetFile = new File(info.getSavePath());
            if(targetFile.exists() && targetFile.isFile()){
                targetFile.delete();
                message +=",删除下载文件";
            }
        }
        Timber.i(message);
    }


    @Override
    public DownloadFileInfor findUnfinishedDownloadingBy(long Id) {
        return downloadInforDao.findUnfinishedDownloadingBy(Id);
    }

    @Override
    public List<DownloadFileInfor> queryDownAll() {
        return downloadInforDao.queryDownAll();
    }
}
