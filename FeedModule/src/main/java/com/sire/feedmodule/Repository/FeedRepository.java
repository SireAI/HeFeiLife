package com.sire.feedmodule.Repository;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.corelibrary.Utils.ObjectMapConversionUtils;
import com.sire.feedmodule.DB.Dao.FeedDao;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.DB.Entry.FeedPraiseInfor;
import com.sire.feedmodule.DB.Entry.Following;
import com.sire.feedmodule.DB.Entry.UserFeedInfor;
import com.sire.feedmodule.Pojo.FeedPraiseUser;
import com.sire.feedmodule.Pojo.FeedPraiseUserRequestInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;
import com.sire.feedmodule.WebService.FeedWebService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

import static com.sire.feedmodule.Constant.Constant.FEED_INFOR;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class FeedRepository {
    private final FeedDao feedDao;
    private final FeedWebService webService;

    @Inject
    public FeedRepository(FeedDao feedDao, FeedWebService webService) {
        this.feedDao = feedDao;
        this.webService = webService;
    }


    public LiveData fetchFeedsInfor(FeedRequestInfor feedRequestInfor) {
        Map<String, Object> params = ObjectMapConversionUtils.Object2Map(feedRequestInfor);
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(feedRequestInfor.dataFromStrategy)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<FeedInfor>>, List<FeedInfor>>() {
                    @Override
                    public LiveData<List<FeedInfor>> loadFromDb() {
                        if (feedRequestInfor.getFeedType().equals(FEED_INFOR)) {
                            return feedDao.queryFeedInfors(feedRequestInfor.getTimeLine().getTime(), feedRequestInfor.getPageSize());
                        } else {
                            //user feed
                            return feedDao.queryUserFeedInfors(feedRequestInfor.getTimeLine().getTime(), feedRequestInfor.getPageSize(), feedRequestInfor.getUserId());
                        }
                    }

                    @Override
                    public LiveData<Response<JsonResponse<List<FeedInfor>>>> makeNetCall() {
                        return webService.fetchFeedsInfor(params);
                    }

                    @Override
                    public void saveData2DB(List<FeedInfor> infors) {
                        if (feedRequestInfor.getFeedType().equals(FEED_INFOR)) {
                            feedDao.insertFeedInfor(infors);
                        } else {
                            //user feed
                            List<UserFeedInfor> userFeedInfors = new ArrayList<>();
                            for (int i = 0; i < infors.size(); i++) {
                                UserFeedInfor userFeedInfor = new UserFeedInfor(infors.get(i));
                                userFeedInfor.setLocalUserId(feedRequestInfor.getUserId());
                                userFeedInfors.add(userFeedInfor);
                            }
                            feedDao.insertUserFeedInfor(userFeedInfors);
                        }
                    }
                });
    }

    public LiveData getAllFollowingsBy(String userId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE_NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<Following>>, List<Following>>() {
                    @Override
                    public LiveData<List<Following>> loadFromDb() {
                        return feedDao.queryFollowingInfors(userId);
                    }

                    @Override
                    public LiveData<Response<JsonResponse<List<Following>>>> makeNetCall() {
                        return webService.fetchFollowingInfor(userId);
                    }

                    @Override
                    public void saveData2DB(List<Following> infors) {
                        feedDao.insertFollowingInfors(infors);
                    }
                });
    }

    public LiveData<DataResource<Following>> getFollowingBy(String userId, String authorId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<Following>, Following>() {
                    @Override
                    public LiveData<Following> loadFromDb() {
                        return feedDao.findFollowingBy(userId, authorId);
                    }
                });
    }

    public LiveData<DataResource<FeedPraiseInfor>> getFeedPraiseInforBy(String userId, String feedId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<FeedPraiseInfor>, FeedPraiseInfor>() {
                    @Override
                    public LiveData<FeedPraiseInfor> loadFromDb() {
                        return feedDao.findFeedPraiseInforBy(userId, feedId);
                    }
                });
    }


    public LiveData follow(Following following) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<Following>, Following>() {
                    @Override
                    public LiveData<Response<JsonResponse<Following>>> makeNetCall() {
                        return webService.follow(following);
                    }

                    @Override
                    public void saveData2DB(Following following) {
                        feedDao.insertFollowingInfors(Arrays.asList(new Following[]{following}));
                    }
                });
    }

    public LiveData<DataResource> cancelFollow(Following following) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<Following>, Following>() {
                           @Override
                           public LiveData<Response<JsonResponse<Following>>> makeNetCall() {
                               return webService.cancelFollow(following);
                           }

                           @Override
                           public void saveData2DB(Following following) {
                               feedDao.deleteFollowingBy(following);
                           }
                       }
                );
    }


    public LiveData<DataResource<List<Following>>> observeDBDataChange(String userId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE)
                .listenDB()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<Following>>, List<Following>>() {
                    @Override
                    public LiveData<List<Following>> loadFromDb() {
                        return feedDao.queryFollowingInfors(userId);
                    }
                });
    }


    public LiveData<DataResource<FeedPraiseInfor>> tickPraise(boolean praised, FeedPraiseInfor feedPraise) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<FeedPraiseInfor>, FeedPraiseInfor>() {
                           @Override
                           public LiveData<Response<JsonResponse<FeedPraiseInfor>>> makeNetCall() {
                               if (praised) {
                                   return webService.praiseFeed(feedPraise);
                               } else {
                                   return webService.cancelPraiseFeed(feedPraise);
                               }
                           }

                           @Override
                           public void saveData2DB(FeedPraiseInfor feedPraiseInfor) {
                               if (praised) {
                                   feedDao.insertFeedPraiseInfor(feedPraiseInfor);
                               } else {
                                   feedDao.deleteFeedPraiseInfor(feedPraiseInfor);
                               }
                           }
                       }
                );
    }

    public LiveData<DataResource<FeedPraiseUser>> getFeedPraiseInfor(FeedPraiseUserRequestInfor feedPraiseUserRequestInfor) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<FeedPraiseUser>, FeedPraiseUser>() {
                           @Override
                           public LiveData<Response<JsonResponse<FeedPraiseUser>>> makeNetCall() {
                               return webService.getfeedpraiseinfor(feedPraiseUserRequestInfor);
                           }
                       }
                );
    }


    public LiveData<DataResource<JsonResponse>> deleteFeed(String feedId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, JsonResponse>() {
                           @Override
                           public LiveData<Response<JsonResponse>> makeNetCall() {
                               return webService.deleteFeed(feedId);
                           }

                       }
                );
    }

    public void deleteCacheFeed(String feedId) {
        FeedInfor feedInfor = feedDao.findFeedInforBy(feedId);
        if (feedInfor != null) {
            feedDao.deleteFeed(feedInfor);
        }
    }
}
