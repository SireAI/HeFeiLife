package com.sire.feedmodule.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.DB.Entry.FeedPraiseInfor;
import com.sire.feedmodule.DB.Entry.Following;
import com.sire.feedmodule.DB.Entry.UserFeedInfor;

import java.util.List;


@Dao
public interface FeedDao {
    //--------------Feed---------------//

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeedInfor(List<FeedInfor> feedInfors);

    @Query("SELECT * FROM FeedInfor WHERE timeLine < :timeLine ORDER BY timeLine DESC LIMIT :pageSize")
    LiveData<List<FeedInfor>> queryFeedInfors(Long timeLine, int pageSize);

    @Delete()
    void deleteFeedsBy(List<FeedInfor> feedInfors);


    //--------------User Feed------------//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserFeedInfor(List<UserFeedInfor> feedInfors);

    @Query("SELECT authorId," +
            " authorName," +
            " feedId, " +
            "feedType, " +
            "title, " +
            "content," +
            " pictureUrls," +
            " videoUrl, " +
            "authorIcon," +
            " categary, " +
            "praiseCount, " +
            "timeLine," +
            " authorLevel," +
            " publishAddress " +
            " FROM UserFeedInfor WHERE timeLine < :timeLine  AND localUserId = :userId ORDER BY timeLine DESC LIMIT :pageSize")
    LiveData<List<FeedInfor>> queryUserFeedInfors(Long timeLine, int pageSize, String userId);

    @Query("SELECT authorId," +
            " authorName," +
            " feedId, " +
            "feedType, " +
            "title, " +
            "content," +
            " pictureUrls," +
            " videoUrl, " +
            "authorIcon," +
            " categary, " +
            "praiseCount, " +
            "timeLine," +
            " authorLevel," +
            " publishAddress " +
            " FROM UserFeedInfor WHERE timeLine < :timeLine   ORDER BY timeLine DESC LIMIT :pageSize")
    LiveData<List<FeedInfor>> queryAllUserFeedInfors(Long timeLine, int pageSize);

    @Delete()
    void deleteUserFeedsBy(List<UserFeedInfor> feedInfors);

    //--------------User Following------------//
    @Query("SELECT * FROM Following WHERE userId = :userId")
    LiveData<List<Following>> queryFollowingInfors(String userId);
    @Query("SELECT * FROM Following WHERE userId = :userId AND followingId = :followingId")
    LiveData<Following> findFollowingBy(String userId,String followingId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFollowingInfors(List<Following> followings);

    @Delete()
    void deleteFollowingBy(Following following);
    //--------------User Feed Praise------------//
    @Query("SELECT * FROM FeedPraiseInfor WHERE userId = :userId AND feedId = :feedId")
    LiveData<FeedPraiseInfor> findFeedPraiseInforBy(String userId, String feedId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFeedPraiseInfor(FeedPraiseInfor feedPraiseInfor);
    @Delete()
    void deleteFeedPraiseInfor(FeedPraiseInfor feedPraiseInfor);
    @Delete()
    void deleteFeed(FeedInfor feedInfor);
    @Query("SELECT * FROM FeedInfor WHERE feedId = :feedId")
    FeedInfor findFeedInforBy(String feedId);
    @Query("SELECT * FROM UserFeedInfor WHERE authorId = :followingId")
    List<UserFeedInfor> findUserFeedByAuthorId(String followingId);


}