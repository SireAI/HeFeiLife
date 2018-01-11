package com.sire.usermodule.WebService;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.Pojo.UserAuth;
import com.sire.usermodule.Pojo.UserLoginInfo;
import com.sire.usermodule.Pojo.UserRegisterInfo;

import java.io.File;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
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
    @POST("user/login")
    LiveData<Response<JsonResponse<User>>> userLogin(@Body UserLoginInfo userLoginInfo);
    @POST("user/create")
    LiveData<Response<JsonResponse<User>>> userRegister(@Body UserRegisterInfo userRegisterInfo);
    @POST()
    LiveData<Response<JsonResponse<User>>> uploadFile(@Url String imageUrl , @Body RequestBody Body);
    @POST("user/updateinfo")
    LiveData<Response<JsonResponse<User>>> updateUserInfor(@Body User user);
    @POST("user/updatepwd")
    LiveData<Response<JsonResponse>> updateUserPassword(@Body UserAuth userAuth);
    @GET("user/getuserinfor")
    LiveData<Response<JsonResponse<User>>> getUserInfor(@Query("userId") String userId,@Query("feedinfor") boolean feedInfor);
}
