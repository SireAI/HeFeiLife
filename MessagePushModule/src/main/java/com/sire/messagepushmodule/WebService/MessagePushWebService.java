package com.sire.messagepushmodule.WebService;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.messagepushmodule.DB.Entry.IMMessage;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/22
 * Author:Sire
 * Description:
 * ==================================================
 */
public interface MessagePushWebService {
    @POST("messagepush/messagebind")
    Flowable<Response<JsonResponse>> messageBind(@Query("userId") String userId, @Query("clientId") String clientId);
    @POST("feed/imtalk")
    LiveData<Response<JsonResponse<IMMessage>>> imtalk(@Body IMMessage imMessage);
}
