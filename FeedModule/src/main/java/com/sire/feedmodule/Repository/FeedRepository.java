package com.sire.feedmodule.Repository;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.corelibrary.Utils.ObjectMapConversionUtils;
import com.sire.feedmodule.DB.Dao.FeedDao;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;
import com.sire.feedmodule.WebService.FeedWebService;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import retrofit2.Response;

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
                .cacheData(true)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<FeedInfor>>, List<FeedInfor>>() {
                    @Override
                    public LiveData<List<FeedInfor>> loadFromDb() {
                        return feedDao.queryFeedInfors(feedRequestInfor.getTimeLine().getTime(),feedRequestInfor.getPageSize());
                    }

                    @Override
                    public LiveData<Response<JsonResponse<List<FeedInfor>>>> makeNetCall() {
                        return webService.fetchFeedsInfor(params);
                    }

                    @Override
                    public void saveData2DB(List<FeedInfor> infors) {
                        feedDao.insert(infors);
                    }
                });
    }

}
