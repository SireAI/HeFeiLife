package com.sire.feedmodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;
import com.sire.feedmodule.Repository.FeedRepository;
import com.sire.feedmodule.Utils.DateKeyUtils;
import com.sire.mediators.UserModuleInterface.UserMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.sire.feedmodule.Constant.Constant.FEED_INFOR;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */
public class FeedViewModel extends ViewModel {
    private final LiveData<DataResource<List<FeedInfor>>> feedInfors;
    UserMediator userMediator;
    private MutableLiveData<FeedRequestInfor> feedInforMutable = new MutableLiveData<>();
    private int newFeedInforStartIndex = 0;
    private int pageSize = 10;
    private DataSourceStrategy.DataFromStrategy dataSourceStrategy;


    @Inject
    public FeedViewModel(FeedRepository feedRepository, UserMediator userMediator) {
        feedInfors = Transformations.switchMap(feedInforMutable, feedRequestInfor -> {
            if (feedRequestInfor == null) {
                return AbsentLiveData.create();
            } else {
                return feedRepository.fetchFeedsInfor(feedRequestInfor);
            }
        });
        this.userMediator = userMediator;
        //获取以前时间为时间线的历史数据
        getHistoryFeedInfor(new Date());
    }

    public void getNewFeedInfor(Date timeLine) {
        getFeedInfor(timeLine, DataSourceStrategy.DataFromStrategy.NET, true);
    }

    private void getFeedInfor(Date timeLine, DataSourceStrategy.DataFromStrategy dataSourceStrategy, boolean getNewFeeds) {
        FeedRequestInfor feedRequestInfor = new FeedRequestInfor();
        feedRequestInfor.setFeedType(FEED_INFOR);
        feedRequestInfor.setUserId(userMediator.getUserId());
        feedRequestInfor.setPageIndex(newFeedInforStartIndex);
        feedRequestInfor.setPageSize(pageSize);
        feedRequestInfor.setGetNewFeeds(getNewFeeds);
        feedRequestInfor.setTimeLine(timeLine);
        feedRequestInfor.setDataFromStrategy(dataSourceStrategy);
        this.dataSourceStrategy = dataSourceStrategy;
        feedInforMutable.setValue(feedRequestInfor);
    }

    /**
     * 以当前时间的0点为基线从优先从缓存中获取历史数据，没有就从服务器获取数据
     */
    public void getHistoryFeedInfor(Date timeLine) {
        getFeedInfor(timeLine, DataSourceStrategy.DataFromStrategy.CACHE_NET, false);
    }

    public LiveData<DataResource<List<FeedInfor>>> getFeedInfors() {
        return feedInfors;
    }


    /**
     * 处理数据集合并
     *
     * @param oldFeedInfors
     * @param listDataResource
     * @return
     */
    @NonNull
    public List<FeedInfor> collectFeedDataList(List<FeedInfor> oldFeedInfors, DataResource<List<FeedInfor>> listDataResource, RefreshNew refreshNew) {
        List<FeedInfor> newFeedInfors = new ArrayList<>();
        if (oldFeedInfors != null) {
            newFeedInfors.addAll(oldFeedInfors);
        }
        if (dataSourceStrategy == DataSourceStrategy.DataFromStrategy.NET) {
            Collections.reverse(listDataResource.data);
            newFeedInfors.addAll(0, listDataResource.data);
            if(refreshNew!=null){
                refreshNew.onNew(listDataResource.data.size());
            }
        } else if (dataSourceStrategy == DataSourceStrategy.DataFromStrategy.CACHE_NET) {
            if(listDataResource.data.size()>0){
                newFeedInfors.addAll(listDataResource.data);
            }else {
                refreshNew.onNoMore();
            }
        }else {
            throw new RuntimeException("缓存策略未设置");
        }
        Collections.sort(newFeedInfors, new Comparator<FeedInfor>() {
            @Override
            public int compare(FeedInfor o1, FeedInfor o2) {
                return (int) (o2.getTimeLine().getTime()-o1.getTimeLine().getTime());
            }
        });
        return newFeedInfors;
    }

    /**
     * 获取拉取最新feed的时间基线，注意集合中的元素必须以timeline时间降序排序，第一个元素的时间
     * 最大
     *
     * @param feedInfors
     * @return
     */
    public Date getMaxTimeLineBy(List<FeedInfor> feedInfors) {
        long currentDateTimeStartLine = DateKeyUtils.generateOffsetdayKey(new Date(), 0);
        if (feedInfors == null || feedInfors.size() == 0) {
            //取当天时间00:00:00为时间基线
            return new Date(currentDateTimeStartLine);
        } else {
            //比较最新feed与当前时间，优先获取今天的feed信息
            Date timeLine = feedInfors.get(0).getTimeLine();
            long max = Math.max(timeLine.getTime(), currentDateTimeStartLine);
            return new Date(max);
        }
    }

    /**
     * 获取拉取历史feed的时间基线，注意集合中的元素必须以timeline时间降序排序，最后一个元素的时间
     * 最小
     *
     * @param feedInfors
     * @return
     */
    public Date getMinTimeLineBy(List<FeedInfor> feedInfors) {
        long currentDateTimeStartLine = DateKeyUtils.generateOffsetdayKey(new Date(), 0);
        if (feedInfors == null || feedInfors.size() == 0) {
            //取当天时间00:00:00为时间基线
            return new Date(currentDateTimeStartLine);
        } else {
            Date timeLine = feedInfors.get(feedInfors.size() - 1).getTimeLine();
            return timeLine;
        }
    }

    public interface RefreshNew {
        void onNew(int count);
        void onNoMore();
    }
}
