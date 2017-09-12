package com.sire.corelibrary.Networking.downlaod.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;

import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/04
 * Author:Sire
 * Description:
 * ==================================================
 */
@Dao
public interface DownloadInforDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveOrUpdate(DownloadFileInfor info);

    @Insert
    void save(DownloadFileInfor info);

    @Update
    void update(DownloadFileInfor info);

    @Delete
    void deleteDowninfo(DownloadFileInfor info);

    @Query("SELECT * FROM DownloadFileInfor WHERE id = :id")
    DownloadFileInfor findUnfinishedDownloadingBy(long id);

    @Query("SELECT * FROM DownloadFileInfor")
    List<DownloadFileInfor> queryDownAll();
}
