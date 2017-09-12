package com.sire.sharemodule.ViewModule;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.sire.mediators.core.CallBack;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/08
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class ThirdPartyLoginModel {
    private final Application context;

    @Inject
    public ThirdPartyLoginModel(Application app) {
        this.context = app;
    }

    public void thirdPartyLogin(String platformName, final CallBack<Object[]> callBack) {

        if (platformName == null) {
            Timber.e("platform name 不能为空");
            return;
        }
        Context application = context.getApplicationContext();

        //初始化SDK
        if (!(application instanceof MobApplication)) {
            MobSDK.init(context.getApplicationContext());
        }

        Platform plat = ShareSDK.getPlatform(platformName);
        if (plat == null) {
            Timber.e(platformName + " platform 不存在");
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
            return;
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Object[] objects = {plat.getName(), res};
                    callBack.apply(objects);
                } else {
                    callBack.apply(null);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    String text = "caught error: " + t.getMessage();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
                callBack.apply(null);
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
                }
                callBack.apply(null);
            }
        });

        plat.showUser(null);

    }


    public boolean isPlatformExist(String platformName) {
        if (TextUtils.isEmpty(platformName)) {
            Timber.e("platform name 不能为空");
            return false;
        }

        Context application = context.getApplicationContext();

        //初始化SDK
        if (!(application instanceof MobApplication)) {
            MobSDK.init(context.getApplicationContext());
        }

        Platform platform = ShareSDK.getPlatform(platformName);
        if (platform == null) {
            Timber.e(platformName + " platform 不存在");
            return false;
        }
        if ("Wechat".equals(platformName)) {
            return platform.isClientValid();
        }
        //以下平台不支持登陆功能
        return !("WechatMoments".equals(platformName)
                || "WechatFavorite".equals(platformName) || "ShortMessage".equals(platformName)
                || "Email".equals(platformName)
                || "Pinterest".equals(platformName) || "Yixin".equals(platformName)
                || "YixinMoments".equals(platformName)
                || "Bluetooth".equals(platformName) || "WhatsApp".equals(platformName)
                || "Pocket".equals(platformName) || "BaiduTieba".equals(platformName)
                || "Laiwang".equals(platformName) || "LaiwangMoments".equals(platformName)
                || "Alipay".equals(platformName) || "AlipayMoments".equals(platformName)
                || "FacebookMessenger".equals(platformName) || "Dingding".equals(platformName)
                || "Youtube".equals(platformName) || "Meipai".equals(platformName));
    }
}
