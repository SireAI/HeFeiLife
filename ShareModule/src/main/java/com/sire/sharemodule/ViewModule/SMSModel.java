package com.sire.sharemodule.ViewModule;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.mob.MobApplication;
import com.mob.MobSDK;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.mediators.core.CallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.inject.Inject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import timber.log.Timber;

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
    private final Map<String, String> errors;


    @Inject
    public SMSModel(Application app) {
        this.context = app;
        errors = new HashMap<>();
        errors.put("460","没有打开客户端发送短信的开关");
        errors.put("461","没有开通给当前地区发送短信的功能");
        errors.put("463","手机号码每天发送次数超限");
        errors.put("464","每台手机每天发送次数超限");
        errors.put("465","号码在App中每天发送短信的次数超限");
        errors.put("466","校验的验证码为空");
        errors.put("467","校验验证码请求频繁");
        errors.put("468","需要校验的验证码错误");
        errors.put("477","当前手机号发送短信的数量超过限额");
        errors.put("478","当前手机号在当前应用内发送超过限额");
        errors.put("500","服务器内部错误");
    }

    /**
     * int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
     * int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
     * int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
     * int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
     * int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
     */
    public void phoneCodeVerifyWithPage(final CallBack<String> callBack) {
        initSDK(callBack);

        // 打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {

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

    public void phoneCodeVerify(String phoneNumber, final CallBack<String> callBack) {
        initSDK(callBack);
        SMSSDK.getVerificationCode("86", phoneNumber);
    }

    public void sendVerifyCode(String phoneNumber, String code) {
        SMSSDK.submitVerificationCode("86", phoneNumber, code);
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
                Timber.d("event = [" + event + "], result = [" + result + "], data = [" + data + "]");
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                        String country = (String) phoneMap.get("country");
                        String phone = (String) phoneMap.get("phone");
                        registerUser(country, phone);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
//                        boolean smart = (Boolean) data;
//                        if (smart) {
//                            //通过智能验证
//                            callBack.apply("success");
//                        }
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    } else if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
                        //验证成功
                        callBack.apply("success");
                    }
                } else {
                    String status = JSONUtils.getJsonValue(((Throwable) data).getMessage(), "status");

                    callBack.apply(errors.get(status));
                    ((Throwable) data).printStackTrace();
                }

            }

        };
        // 注册回调监听接口
        unregister();
        SMSSDK.registerEventHandler(eventHandler);
    }

    // 提交用户信息
    private void registerUser(String country, String phone) {
        String uid = phone;
        String nickName = phone;
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
