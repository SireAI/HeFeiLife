package com.sire.upgrademodule.WebService;





import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Pojo.JsonResponse;
import com.sire.upgrademodule.Pojo.UpgradeInfor;
import com.sire.upgrademodule.Pojo.VersionInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/04
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface AppUpgradeWebService {

    @Headers({"Content-Type:application/json","Cache-Control:no-cache"})
    @Streaming
    @GET("/upgrade/checkupgrade")
    Observable<JsonResponse<UpgradeInfor>> checkAPPVersion(@QueryMap Map<String, Object> params);

}
