package com.sire.feedmodule.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.feedmodule.DB.Dao.FeedDao;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.DB.Entry.UserFeedInfor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class CacheClearRepository {
    private final FeedDao feedDao;
    private final AppExecutors appExecutors;

    @Inject
    public CacheClearRepository(FeedDao feedDao, AppExecutors appExecutors) {
        this.feedDao = feedDao;
        this.appExecutors = appExecutors;
    }

    /**
     * 删除特定时间线以前的feed信息
     * @param date
     */
    public void clearFeedsCacheBy(Date date) {
        //feed信息删除
        LiveData<List<FeedInfor>> feedsInfor = feedDao.queryFeedInfors(date.getTime(), Integer.MAX_VALUE);
        feedsInfor.observeForever(new Observer<List<FeedInfor>>() {
            @Override
            public void onChanged(@Nullable List<FeedInfor> feedInfors) {
                feedsInfor.removeObserver(this);
                if(feedInfors!=null && feedInfors.size()>0){
                    appExecutors.diskIO().execute(() -> feedDao.deleteFeedsBy(feedInfors));
                }
            }
        });
        //user feed信息删除
        LiveData<List<FeedInfor>> userFeedsInfor = feedDao.queryAllUserFeedInfors(date.getTime(), Integer.MAX_VALUE);
        userFeedsInfor.observeForever(new Observer<List<FeedInfor>>() {
            @Override
            public void onChanged(@Nullable List<FeedInfor> feedInfors) {
                userFeedsInfor.removeObserver(this);
                if(feedInfors!=null && feedInfors.size()>0){
                    appExecutors.diskIO().execute(() -> {
                        List<UserFeedInfor> userFeedInfors =   new ArrayList<>();
                        for (int i = 0; i < feedInfors.size(); i++) {
                            userFeedInfors.add(new UserFeedInfor(feedInfors.get(i)));
                        }
                        feedDao.deleteUserFeedsBy(userFeedInfors);
                    });
                }
            }
        });

    }
}
