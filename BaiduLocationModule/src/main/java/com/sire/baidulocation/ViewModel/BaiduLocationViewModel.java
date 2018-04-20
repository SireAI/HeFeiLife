package com.sire.baidulocation.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.sire.baidulocation.Service.LocationService;
import com.sire.mediators.core.CallBack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/02
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class BaiduLocationViewModel extends ViewModel {
    private final Application context;
    private final LocationService locationService;
    private LocationResultListener locationResultListener;

    @Inject
    public BaiduLocationViewModel(Application app) {
        this.context = app;
        locationService = new LocationService(app);
    }

    public void registerCommponent(Class<? extends Activity> clazz,CallBack<Map<String, Object>> callBack) {
        locationResultListener = new LocationResultListener(callBack);
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (clazz == activity.getClass()) {
                    serviceOn();
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (clazz == activity.getClass()) {
                    serviceOff();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (clazz == activity.getClass()) {
                    context.unregisterActivityLifecycleCallbacks(this);
                }
            }
        });
    }

    /**
     * 定位启动
     *
     * @param locate
     */
    public void locate(boolean locate) {
        if (locate && !locationService.isStart()) {
            locationService.start();
        } else {
            locationService.stop();
        }
    }

    private void serviceOff() {
        locationService.unregisterListener(locationResultListener);
        locationService.stop();
        locationResultListener = null;
    }

    private void serviceOn() {
        locationService.registerListener(locationResultListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }



    private static class LocationResultListener extends BDAbstractLocationListener{
        private  CallBack<Map<String, Object>> callBack;

        public LocationResultListener(CallBack<Map<String,Object>> callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            Map<String,Object> locateResult = new LinkedHashMap<>();
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                locateResult.put("locateResult","success");
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                locateResult.put("city",location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                locateResult.put("district",location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                locateResult.put("street",location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                locateResult.put("locationdescribe",location.getLocationDescribe());
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                List<String> pois = new LinkedList<>();
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                        pois.add(poi.getName());
                    }
                }
                locateResult.put("pois",pois);
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
            }else {
                locateResult.put("locateResult","failed");
            }

            if(callBack!=null){
                callBack.apply(locateResult);
            }else {
                Timber.e("定位回调未定义");
            }
        }
    }
}
