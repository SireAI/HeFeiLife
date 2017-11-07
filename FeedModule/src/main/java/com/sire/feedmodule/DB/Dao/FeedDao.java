package com.sire.feedmodule.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;

import java.util.Date;
import java.util.List;


@Dao
public interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FeedInfor> feedInfors);

    @Query("SELECT * FROM FeedInfor WHERE timeLine < :timeLine ORDER BY timeLine DESC LIMIT :pageSize")
    LiveData<List<FeedInfor>> queryFeedInfors(Long timeLine, int pageSize);



//
//    @Query("SELECT * FROM FeedInfor WHERE name = :name")
//    LiveData<FeedInfor> queryUserById(String name);
//

//    @Query("SELECT * FROM FeedInfor ORDER BY loginTime DESC")
//    LiveData<List<FeedInfor>> queryAllUsers();
}