package com.sire.bbsmodule.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import com.sire.bbsmodule.DB.Entry.CommentPraiseInfor;

import java.util.List;


@Dao
public interface BBSDao {

    //--------------praise------------//
    @Query("SELECT * FROM CommentPraiseInfor WHERE feedId = :feedId")
    LiveData<List<CommentPraiseInfor>> queryPraiseInfor(String feedId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPraiseInfors(CommentPraiseInfor praiseInfor);

    @Delete()
    void deletePraiseInforBy(CommentPraiseInfor praiseInfor);


}