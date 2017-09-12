package com.sire.sharemodule.ViewModule;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.sire.corelibrary.Utils.UUIDUtils;
import com.sire.mediators.core.CallBack;

import java.util.HashMap;

import javax.inject.Inject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/08
 * Author:Sire
 * Description:
 * ==================================================
 */
public class SMSModel extends ViewModel {

    // 短信注册，随机产生头像
    private static final String[] AVATARS = {
            "http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
            "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
            "http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
            "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
            "http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
            "http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
            "http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
            "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
            "http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
            "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
    };


    private Application context;


    @Inject
    public SMSModel(Application app) {
        this.context = app;
    }

    /**
     * int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
     * int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
     * int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
     * int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
     * int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
     */



    public void phoneCodeVerify(final CallBack<String> callBack) {
        initSDK(callBack);
        // 打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    // 提交用户信息
                    registerUser(country, phone);
                }
            }
        });
        registerPage.show(context);
    }

    private void initSDK(final CallBack<String> callBack) {
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
//        SMSSDK.setAskPermisionOnReadContact(true);
        Context application = context.getApplicationContext();

        //初始化SDK
        if (!(application instanceof MobApplication)) {
            MobSDK.init(context.getApplicationContext());
        }

        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                    // 短信注册成功后，返回MainActivity,然后提示新好友
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        callBack.apply("success");
                    } else {
                        ((Throwable) data).printStackTrace();
                        callBack.apply("failed");
                    }
                }
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    // 提交用户信息
    private void registerUser(String country, String phone) {
        String uid = UUIDUtils.generateSingleId();
        String nickName = "user" + uid;
        String avatar = AVATARS[1];
        SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
    }


    /**
     * destroy
     */

    public void unregister() {
        SMSSDK.unregisterAllEventHandler();
    }


    @Override
    protected void onCleared() {
        unregister();
        context = null;
    }
}
