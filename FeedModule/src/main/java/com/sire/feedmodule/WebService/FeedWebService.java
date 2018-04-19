package com.sire.feedmodule.WebService;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.DB.Entry.FeedPraiseInfor;
import com.sire.feedmodule.DB.Entry.Following;
import com.sire.feedmodule.Pojo.FeedPraiseUser;
import com.sire.feedmodule.Pojo.FeedPraiseUserRequestInfor;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface FeedWebService {
    @GET("feed/getfeeds")
    LiveData<Response<JsonResponse<List<FeedInfor>>>> fetchFeedsInfor(@QueryMap Map<String,Object> params);
    @GET("feed/getfollowings")
    LiveData<Response<JsonResponse<List<Following>>>> fetchFollowingInfor(@Query("userId") String userId);
    @POST("feed/follow")
    LiveData<Response<JsonResponse<Following>>> follow(@Body Following following);
    @POST("feed/canncelfollow")
    LiveData<Response<JsonResponse<Following>>> cancelFollow(@Body Following following);


    @POST("feed/praisefeed")
    LiveData<Response<JsonResponse<FeedPraiseInfor>>> praiseFeed(@Body FeedPraiseInfor feedPraise);
    @POST("feed/cancelpraisefeed")
    LiveData<Response<JsonResponse<FeedPraiseInfor>>> cancelPraiseFeed(@Body FeedPraiseInfor feedPraise);
    @POST("feed/getfeedpraiseinfor")
    LiveData<Response<JsonResponse<FeedPraiseUser>>> getfeedpraiseinfor(@Body FeedPraiseUserRequestInfor feedPraiseUserRequestInfor);
    @POST("feed/deletefeed")
    LiveData<Response<JsonResponse>> deleteFeed(@Query("feedId") String feedId);
    @GET("feed/getsinglefeed")
    LiveData<Response<JsonResponse<FeedInfor>>> getFeedById(@Query("feedId") String feedId);
}
