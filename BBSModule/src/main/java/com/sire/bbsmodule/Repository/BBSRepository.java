package com.sire.bbsmodule.Repository;

import android.arch.lifecycle.LiveData;

import com.sire.bbsmodule.DB.Dao.BBSDao;
import com.sire.bbsmodule.DB.Entry.CommentPraiseInfor;
import com.sire.bbsmodule.Pojo.Comment;
import com.sire.bbsmodule.Pojo.CommentRequestInfor;
import com.sire.bbsmodule.Pojo.PublishInfor;
import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.bbsmodule.WebService.BBSWebService;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.corelibrary.Utils.ObjectMapConversionUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

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
public class BBSRepository {
    private final BBSWebService bbsWebService;
    private final BBSDao bbsDao;

    @Inject
    public BBSRepository(BBSWebService bbsWebService, BBSDao bbsDao) {
        this.bbsWebService = bbsWebService;
        this.bbsDao = bbsDao;
    }


    public LiveData fetchCommentsInfor(CommentRequestInfor commentRequestInfor) {
        Map<String, Object> params = ObjectMapConversionUtils.Object2Map(commentRequestInfor);
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<Comment>>, List<Comment>>() {
                    @Override
                    public LiveData<Response<JsonResponse<List<Comment>>>> makeNetCall() {
                        return bbsWebService.fetchCommentsInfor(params);
                    }
                });
    }

    public LiveData<DataResource> cancelPraise(CommentPraiseInfor praiseInfor) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<CommentPraiseInfor>, CommentPraiseInfor>() {
                           @Override
                           public LiveData<Response<JsonResponse<CommentPraiseInfor>>> makeNetCall() {
                               return bbsWebService.cancelPraise(praiseInfor);
                           }

                           @Override
                           public void saveData2DB(CommentPraiseInfor praiseInfor) {
                               bbsDao.deletePraiseInforBy(praiseInfor);
                           }
                       }
                );
    }

    public LiveData<DataResource> praise(CommentPraiseInfor praiseInfor) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<CommentPraiseInfor>, CommentPraiseInfor>() {
                           @Override
                           public LiveData<Response<JsonResponse<CommentPraiseInfor>>> makeNetCall() {
                               return bbsWebService.praise(praiseInfor);
                           }

                           @Override
                           public void saveData2DB(CommentPraiseInfor praiseInfor) {
                               bbsDao.insertPraiseInfors(praiseInfor);
                           }
                       }
                );
    }

    public LiveData<DataResource<List<CommentPraiseInfor>>> getCommentPraiseInfor(String feedId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE)
                .listenDB()
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<List<CommentPraiseInfor>>, List<CommentPraiseInfor>>() {
                    @Override
                    public LiveData<List<CommentPraiseInfor>> loadFromDb() {
                        return bbsDao.queryPraiseInfor(feedId);
                    }
                });
    }

    public LiveData<DataResource<Comment>> publishComment(Comment comment) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<Comment>, Comment>() {
                           @Override
                           public LiveData<Response<JsonResponse<Comment>>> makeNetCall() {
                               return bbsWebService.publishComment(comment);
                           }

                       }
                );
    }

    public LiveData<DataResource<ReportReason>> report(ReportReason reportReason) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, JsonResponse>() {
                           @Override
                           public LiveData<Response<JsonResponse>> makeNetCall() {
                               return bbsWebService.report(reportReason);
                           }

                       }
                );
    }

    public LiveData<DataResource<JsonResponse>> userDeleteComment(String commentId) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, JsonResponse>() {
                           @Override
                           public LiveData<Response<JsonResponse>> makeNetCall() {
                               return bbsWebService.userDeleteComment(commentId);
                           }

                       }
                );
    }

    public LiveData<DataResource<JsonResponse>> publishPost(PublishInfor post) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, JsonResponse>() {
                           @Override
                           public LiveData<Response<JsonResponse>> makeNetCall() {
                               return bbsWebService.publishPost(post);
                           }

                       }
                );
    }
}

