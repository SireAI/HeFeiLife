package com.sire.corelibrary.DI.Environment;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sire.corelibrary.Networking.downlaod.DB.Dao.DownloadInforDao;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

@Database(entities = {DownloadFileInfor.class}, version = 1,exportSchema = false)
public abstract class EnvironmentDB extends RoomDatabase {
    public static String ENVIRONMENT = "environment.db";
    abstract public DownloadInforDao downloadInforDao();
}