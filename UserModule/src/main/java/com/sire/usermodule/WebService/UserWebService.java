package com.sire.usermodule.WebService;

import android.arch.lifecycle.LiveData;

import com.sire.usermodule.DB.Entry.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface UserWebService {
    @Headers({"Content-Type:application/json","Cache-Control:no-cache"})
    @POST("lovepet/login")
    LiveData<Response<User>> userLogin(@Body User user);
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
