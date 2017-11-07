package com.sire.feedmodule.WebService;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.feedmodule.DB.Entry.FeedInfor;
import com.sire.feedmodule.Pojo.FeedRequestInfor;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    @GET("feed/getFeeds")
    LiveData<Response<JsonResponse<List<FeedInfor>>>> fetchFeedsInfor(@QueryMap Map<String,Object> params);
//    @POST("user/create")
//    LiveData<Response<JsonResponse<User>>> userRegister(@Body UserRegisterInfo userRegisterInfo);
}
