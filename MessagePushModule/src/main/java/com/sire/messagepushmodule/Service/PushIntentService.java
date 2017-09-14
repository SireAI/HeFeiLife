package com.sire.messagepushmodule.Service;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import timber.log.Timber;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class PushIntentService extends GTIntentService {

    private static final String TAG = "PushIntentService";



    public PushIntentService() {

    }

    /**
     * 初始化时将获得pid
     * @param context
     * @param pid
     */
    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    /**
     * appid为应用注册id,taskid,messageid,为系统定义的id,payload为透传消息数据，pkgname为应用id,clientid为用户标示
     * 注意区别通知与透传消息
     *
     * @param context
     * @param msg
     */
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();
        Timber.d("onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid+ "\npayload = " + new String(payload));
    }

    /**
     * 初始化SDK后将从个推服务器获得clientid,用于标记用户
     *
     * @param context
     * @param clientid
     */
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Timber.d("onReceiveClientId -> " + "clientid = " + clientid);

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Timber.d("onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Timber.d("onReceiveCommandResult -> " + cmdMessage);
        
    }


}
