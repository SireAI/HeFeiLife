package com.sire.feedmodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.corelibrary.Networking.dataBound.DataStatus;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.DB.Entry.FeedPraiseInfor;
import com.sire.feedmodule.DB.Entry.Following;
import com.sire.feedmodule.Pojo.FeedPraiseUser;
import com.sire.feedmodule.Pojo.FeedPraiseUserRequestInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;
import com.sire.feedmodule.Repository.FeedRepository;
import com.sire.feedmodule.Utils.DateKeyUtils;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

import static com.sire.feedmodule.Constant.Constant.USER_DYNAMICS;
import static com.sire.feedmodule.Constant.Constant.USER_FEED;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/31
 * Author:Sire
 * Description:
 * ==================================================
 */
public class FeedViewModel extends ViewModel {
    public static final int PAGE_SIZE = 10;
    private final LiveData<DataResource<List<FeedInfor>>> feedInfors;
    private final FeedRepository feedRepository;
    private final AppExecutors appExecutors;
    UserMediator userMediator;
    private MutableLiveData<FeedRequestInfor> feedInforMutable = new MutableLiveData<>();
    private int newFeedInforStartIndex = 0;
    private int pageSize = 10;
    private DataSourceStrategy.DataFromStrategy dataSourceStrategy;


    @Inject
    public FeedViewModel(FeedRepository feedRepository, UserMediator userMediator, AppExecutors appExecutors) {
        feedInfors = Transformations.switchMap(feedInforMutable, feedRequestInfor -> {
            if (feedRequestInfor == null) {
                return AbsentLiveData.create();
            } else {
                return feedRepository.fetchFeedsInfor(feedRequestInfor);
            }
        });
        this.userMediator = userMediator;
        this.feedRepository = feedRepository;
        this.appExecutors = appExecutors;

    }

    public LiveData<DataResource<List<Following>>> getFollowings() {

        return feedRepository.getAllFollowingsBy(userMediator.getUserId());
    }

    public LiveData<DataResource<List<Following>>> observeDBDataChange() {
        return feedRepository.observeDBDataChange(userMediator.getUserId());
    }

    public void getNewFeedInfor(Date timeLine, String feedType) {
        getFeedInfor(timeLine, DataSourceStrategy.DataFromStrategy.NET, true, feedType, "");
    }

    private void getFeedInfor(Date timeLine, DataSourceStrategy.DataFromStrategy dataSourceStrategy, boolean getNewFeeds, String feedType, String userDynamicId) {
        FeedRequestInfor feedRequestInfor = new FeedRequestInfor();
        feedRequestInfor.setFeedType(feedType);
        if (feedType.equals(USER_FEED)) {
            feedRequestInfor.setUserId(userMediator.getUserId());
        }else if(feedType.equals(USER_DYNAMICS)){
            feedRequestInfor.setUserId(userDynamicId);
        }
        feedRequestInfor.setPageIndex(newFeedInforStartIndex);
        feedRequestInfor.setPageSize(pageSize);
        feedRequestInfor.setGetNewFeeds(getNewFeeds);
        feedRequestInfor.setTimeLine(timeLine);
        feedRequestInfor.setDataFromStrategy(dataSourceStrategy);
        this.dataSourceStrategy = dataSourceStrategy;
        feedInforMutable.setValue(feedRequestInfor);
    }

    public LiveData<DataResource> follow(String followingId) {
        Following following = new Following();
        following.setFollowingId(followingId);
        following.setUserId(userMediator.getUserId());
        return feedRepository.follow(following);
    }


    /**
     * 以当前时间的0点为基线从优先从缓存中获取历史数据，没有就从服务器获取数据
     */
    public void getHistoryFeedInfor(Date timeLine, String feedType) {
        getFeedInfor(timeLine, DataSourceStrategy.DataFromStrategy.CACHE_NET, false, feedType, "");
    }
    public void getHistoryFeedInfor(Date timeLine, String feedType,String userDynamicId) {
        getFeedInfor(timeLine, DataSourceStrategy.DataFromStrategy.CACHE_NET, false, feedType,userDynamicId);
    }

    public LiveData<DataResource<List<FeedInfor>>> getFeedInfors() {
        return feedInfors;
    }


    /**
     * 处理数据集合并
     *
     * @param oldFeedInfors
     * @param comeFeedInfors
     * @return
     */
    @NonNull
    public List<FeedInfor> collectFeedDataList(List<FeedInfor> oldFeedInfors, List<FeedInfor> comeFeedInfors, RefreshNew refreshNew) {
        List<FeedInfor> newFeedInfors = new ArrayList<>();
        if (oldFeedInfors != null) {
            newFeedInfors.addAll(oldFeedInfors);
        }
        if (dataSourceStrategy == DataSourceStrategy.DataFromStrategy.NET) {
            if (comeFeedInfors != null && comeFeedInfors.size() >= 0) {
                newFeedInfors.addAll(0, comeFeedInfors);
                refreshNew.onNew(comeFeedInfors.size());
            }

        } else if (dataSourceStrategy == DataSourceStrategy.DataFromStrategy.CACHE_NET) {
            if (comeFeedInfors!=null&&comeFeedInfors.size() > 0) {
                newFeedInfors.addAll(comeFeedInfors);
            } else {
                refreshNew.onNoMore();
            }
        } else {
            throw new RuntimeException("缓存策略未设置");
        }
        Collections.sort(newFeedInfors, (o1, o2) -> (int) (o2.getTimeLine().getTime() - o1.getTimeLine().getTime()));
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
        if (feedInfors == null || feedInfors.size() == 0) {
            //离当前时间最近的数据
            return new Date();
        } else {
            Date timeLine = feedInfors.get(feedInfors.size() - 1).getTimeLine();
            return timeLine;
        }
    }

    public LiveData<DataResource> cancelFollow(String followingId) {

        Following following = new Following();
        following.setFollowingId(followingId);
        following.setUserId(userMediator.getUserId());
        return feedRepository.cancelFollow(following);
    }

    /**
     * 点、取消赞
     *
     * @param praised
     * @param feedId
     * @param callBack
     * @return
     */
    public void tickPraise(boolean praised, String feedId, CallBack<Boolean> callBack) {
        FeedPraiseInfor feedPraise = new FeedPraiseInfor();
        feedPraise.setFeedId(feedId);
        feedPraise.setUserId(userMediator.getUserId());
        feedPraise.setUserImage(userMediator.getUserImage());
        feedPraise.setUserName(userMediator.getUserName());
        LiveData<DataResource<FeedPraiseInfor>> tickPraise = feedRepository.tickPraise(praised, feedPraise);
        tickPraise.observeForever(new Observer<DataResource<FeedPraiseInfor>>() {
            @Override
            public void onChanged(@Nullable DataResource<FeedPraiseInfor> feedPraiseInforDataResource) {
                switch (feedPraiseInforDataResource.status) {
                    case SUCCESS:
                        tickPraise.removeObserver(this);
                        callBack.apply(true);
                        break;
                    case ERROR:
                        tickPraise.removeObserver(this);

                        callBack.apply(false);
                        break;
                    case LOADING:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 查找是否存在点赞关注信息
     *
     * @param feedId
     * @param callBack
     */
    public void getFeedFocusInfor(String authorId, String feedId, CallBack<Map<String, Boolean>> callBack) {
        Map<String, Boolean> feedFcusInfors = new HashMap<>();
        //关注
        LiveData<DataResource<Following>> followingData = feedRepository.getFollowingBy(userMediator.getUserId(), authorId);
        //点赞
        LiveData<DataResource<FeedPraiseInfor>> feedPraiseInforData = feedRepository.getFeedPraiseInforBy(userMediator.getUserId(), feedId);
        followingData.observeForever(new Observer<DataResource<Following>>() {
            @Override
            public void onChanged(@Nullable DataResource<Following> followingDataResource) {
                if (followingDataResource.status == DataStatus.SUCCESS) {
                    followingData.removeObserver(this);
                    feedFcusInfors.put("following", followingDataResource.data != null);
                    feedPraiseInforData.observeForever(new Observer<DataResource<FeedPraiseInfor>>() {
                        @Override
                        public void onChanged(@Nullable DataResource<FeedPraiseInfor> feedPraiseInforDataResource) {
                            if (feedPraiseInforDataResource.status == DataStatus.SUCCESS) {
                                feedPraiseInforData.removeObserver(this);
                                feedFcusInfors.put("feedPraiseInfor", feedPraiseInforDataResource.data != null);
                                callBack.apply(feedFcusInfors);
                            }

                        }
                    });
                }

            }
        });
    }

    public void getFeedPraiseInfor(String feedId, CallBack<String> callBack) {
        FeedPraiseUserRequestInfor feedPraiseUserRequestInfor = new FeedPraiseUserRequestInfor();
        feedPraiseUserRequestInfor.setFeedId(feedId);
        feedPraiseUserRequestInfor.setPageSize(PAGE_SIZE);
        LiveData<DataResource<FeedPraiseUser>> feedPraiseInfor = feedRepository.getFeedPraiseInfor(feedPraiseUserRequestInfor);
        feedPraiseInfor.observeForever(new Observer<DataResource<FeedPraiseUser>>() {
            @Override
            public void onChanged(@Nullable DataResource<FeedPraiseUser> feedPraiseUserDataResource) {
                switch (feedPraiseUserDataResource.status) {
                    case SUCCESS:
                        feedPraiseInfor.removeObserver(this);

                        String result = JSONUtils.bean2JsonString(feedPraiseUserDataResource.data);
                        callBack.apply(result);
                        break;
                    case ERROR:
                        feedPraiseInfor.removeObserver(this);

                        Timber.e(feedPraiseUserDataResource.toString());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void deleteFeed(String feedId, CallBack<Boolean> callBack) {
        LiveData<DataResource<JsonResponse>> dataResourceLiveData = feedRepository.deleteFeed(feedId);
        dataResourceLiveData.observeForever(new Observer<DataResource<JsonResponse>>() {
            @Override
            public void onChanged(@Nullable DataResource<JsonResponse> jsonResponseDataResource) {
                switch (jsonResponseDataResource.status) {
                    case SUCCESS:
                        dataResourceLiveData.removeObserver(this);
                        appExecutors.diskIO().execute(() -> {
                            feedRepository.deleteCacheFeed(feedId);
                            appExecutors.mainHandler().post(() -> callBack.apply(true));
                        });
                        break;
                    case ERROR:
                        dataResourceLiveData.removeObserver(this);
                        callBack.apply(false);
                        Timber.e(dataResourceLiveData.toString());
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public interface RefreshNew {
        void onNew(int count);

        void onNoMore();
    }
}
