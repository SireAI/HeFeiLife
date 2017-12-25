package com.sire.bbsmodule.WebService;

import android.arch.lifecycle.LiveData;

import com.sire.bbsmodule.DB.Entry.CommentPraiseInfor;
import com.sire.bbsmodule.Pojo.Comment;
import com.sire.bbsmodule.Pojo.PublishInfor;
import com.sire.bbsmodule.Pojo.ReportReason;
import com.sire.corelibrary.Networking.Response.JsonResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface BBSWebService {
    @GET("feed/getComments")
    LiveData<Response<JsonResponse<List<Comment>>>> fetchCommentsInfor(@QueryMap Map<String, Object> params);
    @POST("feed/praisecomment")
    LiveData<Response<JsonResponse<CommentPraiseInfor>>> praise(@Body CommentPraiseInfor praiseInfor);
    @POST("feed/cancelpraisecomment")
    LiveData<Response<JsonResponse<CommentPraiseInfor>>> cancelPraise(@Body CommentPraiseInfor praiseInfor);
    @POST("feed/publishComment")
    LiveData<Response<JsonResponse<Comment>>> publishComment(@Body Comment comment);
    @POST("feed/report")
    LiveData<Response<JsonResponse>> report(@Body ReportReason reportReason);

    @POST("feed/deletecomment")
    LiveData<Response<JsonResponse>> userDeleteComment(@Query(value = "commentId") String commentId);
    @POST("feed/publish")
    LiveData<Response<JsonResponse>> publishPost(@Body PublishInfor post);
}
