package com.sire.messagepushmodule.Service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.WebUrl;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.mediators.UserModuleInterface.UserLoginState;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.messagepushmodule.Pojo.PushInfor;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;
import com.sire.messagepushmodule.WebService.MessagePushWebService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DaggerBroadcastReceiver;
import dagger.android.DaggerIntentService;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;

import static com.sire.messagepushmodule.Constant.Constant.WAIT_TO_VERIRFY_CLIENT_ID;
import static com.sire.messagepushmodule.Pojo.PushInfor.PushEvent.EVENT_SERVER_DATA;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class PushIntentService extends GTIntentService {

    private static final String TAG = "PushIntentService";
;
    @Override
    public void onCreate() {
        super.onCreate();
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
        postMessage(messageid,new String(payload));
    }

    /**
     * 这里使用推送消息ID以防止消息重复推送
     * @param messageid
     * @param message
     */
    private void postMessage(String messageid, String message) {
        PushInfor pushInfor = new PushInfor();
        pushInfor.setPushEvent(EVENT_SERVER_DATA);
        pushInfor.setMessageId(messageid);
        pushInfor.setMessageContent(new String(message));
        EventBus.getDefault().post(pushInfor);
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
        postClientId(clientid);

    }

    private void postClientId(String clientid) {
        PushInfor pushInfor = new PushInfor();
        pushInfor.setPushEvent(PushInfor.PushEvent.EVENT_CLIENT_ID);
        pushInfor.setClientId(clientid);
        EventBus.getDefault().post(pushInfor);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Timber.d("onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Timber.d("onReceiveCommandResult -> " + cmdMessage);
        
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
