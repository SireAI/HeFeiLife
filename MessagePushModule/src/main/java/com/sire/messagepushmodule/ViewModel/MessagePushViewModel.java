package com.sire.messagepushmodule.ViewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.igexin.sdk.PushManager;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.corelibrary.Utils.TimeOffSetUtils;
import com.sire.corelibrary.Utils.UUIDUtils;
import com.sire.mediators.UserModuleInterface.UserLoginState;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;
import com.sire.messagepushmodule.DB.Entry.IMMessage;
import com.sire.messagepushmodule.DB.Entry.Message;
import com.sire.messagepushmodule.Pojo.CommentMessage;
import com.sire.messagepushmodule.Pojo.FeedPraiseMessage;
import com.sire.messagepushmodule.Pojo.MessageRequestInfor;
import com.sire.messagepushmodule.Pojo.PushInfor;
import com.sire.messagepushmodule.Repository.MessageRepository;
import com.sire.messagepushmodule.Service.PushClientService;
import com.sire.messagepushmodule.Service.PushIntentService;
import com.sire.messagepushmodule.Utils.MessageNotifycation;
import com.sire.messagepushmodule.Utils.MessageParser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_COMMENT;
import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_POST;
import static com.sire.messagepushmodule.Constant.Constant.IM_TO_IM;
import static com.sire.messagepushmodule.Constant.Constant.PRAISE_TO_POST;
import static com.sire.messagepushmodule.Constant.Constant.WAIT_TO_VERIRFY_CLIENT_ID;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/12
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class MessagePushViewModel extends ViewModel {
    public static final String DATA = "data";
    /**
     * 消息时间显示阈值
     */
    public static final int TIME_THRESHOLDE = 1*60*1000;
    private static int SETP_PAGE_SIZE = 0;
    private final AppExecutors appExecutors;
    private final UserMediator userMediator;
    private final MessageRepository messageRepository;
    private final LiveData<DataResource<List<Message>>> messageData;
    private Application context;
    private MutableLiveData<MessageRequestInfor> messageRequestInforData = new MutableLiveData<>();
    /**
     * 全局消息数
     */
    private static int messageCount;
    private boolean imSate ;

    @Inject
    public MessagePushViewModel(Application context, AppExecutors appExecutors, UserMediator userMediator, MessageRepository messageRepository) {
        this.context = context;
        this.appExecutors = appExecutors;
        this.userMediator = userMediator;
        this.messageRepository = messageRepository;
        messageData = Transformations.switchMap(messageRequestInforData, messageRequestInfor -> {
            if (messageRequestInfor == null) {
                return AbsentLiveData.create();
            } else {
                return messageRepository.observerMessageDbDataChange(userMediator.getUserId(),messageRequestInfor.getTimeLine().getTime(), messageRequestInfor.getPageSize());
            }
        });
        EventBus.getDefault().register(this);

    }

    public LiveData<DataResource<List<Message>>> getMessageData() {
        return messageData;
    }

    public void initMesageRequest() {
        SETP_PAGE_SIZE = 0;
        requestMoreMessageData();
    }

    public void requestMoreMessageData() {
        MessageRequestInfor messageRequestInfor = new MessageRequestInfor();
        messageRequestInfor.setPageSize(SETP_PAGE_SIZE += 20);
        messageRequestInfor.setTimeLine(TimeOffSetUtils.tommorrow());
        messageRequestInforData.setValue(messageRequestInfor);
    }



    public void setImSate(boolean imSate) {
        this.imSate = imSate;
    }

    /**
     * 初始化推送SDK
     */
    public void initPushSDK() {
        appExecutors.networkIO().execute(() -> {
            //注册核心服务
            PushManager.getInstance().initialize(context, PushClientService.class);
            //注册推送的回调接收服务
            PushManager.getInstance().registerPushIntentService(context.getApplicationContext(), PushIntentService.class);
        });
    }

    /**
     * 开关推送
     *
     * @param on
     */
    public void togglePushService(boolean on) {
        if (on) {
            if (!PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())) {
                PushManager.getInstance().turnOnPush(context.getApplicationContext());
            }
        } else {
            if (PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())) {
                PushManager.getInstance().turnOffPush(context.getApplicationContext());
            }
        }
    }

    /**
     * 关闭推送SDK不连网，
     *
     * @param beginHour    开始时间，设置范围在0-23小时之间，单位 h
     * @param durationHour 持续时间，设置范围在0-23小时之间。持续时间为0则不静默，单位 h
     * @return 是否成功
     */
    public boolean setPushSleepTime(int beginHour, int durationHour) {
        return PushManager.getInstance().setSilentTime(context.getApplicationContext(), beginHour, durationHour);
    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(PushInfor pushInfor) {
        switch (pushInfor.getPushEvent()) {
            case EVENT_CLIENT_ID:
                bindClienId(pushInfor);
                break;
            case EVENT_SERVER_DATA:
                handleMessage(pushInfor);
                break;
            default:
                break;
        }

    }

    private void handleMessage(PushInfor pushInfor) {
        if (pushInfor != null && !TextUtils.isEmpty(pushInfor.getMessageContent())) {
            Message message = JSONUtils.jsonString2Bean(pushInfor.getMessageContent(), Message.class);
            if (message != null) {
                try {
                    message.setUserId(userMediator.getUserId());
                    JSONObject jsonObject = new JSONObject(pushInfor.getMessageContent());
                    if (jsonObject != null) {
                        message.setJsonData(jsonObject.getString(DATA));
                        message.setMessageId(pushInfor.getMessageId());
                        Timber.d("接受到消息：" + message.toString());
                        saveMessage(message);
                        sendNotifycation(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveMessage(Message message) {
        int messageType = message.getMessageType();
        if(messageType==IM_TO_IM){
            Class clazz = MessageParser.registers.get(messageType);
            IMMessage imMessage = (IMMessage)JSONUtils.jsonString2Bean(message.getJsonData(), clazz);
            //对话消息类型
            imMessage.setMessageId(message.getMessageId());
            imMessage.setMessageCreateTime(message.getMessageCreateTime());
            messageTimeShow(imMessage);
            messageRepository.saveIMMessage(imMessage);
            //替换messageId
            message.setMessageId(imMessage.getFromAuthorId());
            //替换时间
            message.setJsonData(JSONUtils.bean2JsonString(imMessage));
        }

        messageRepository.localSave(message);
    }

    /**
     * 保存发送者发送的信息到本地
     * @param imMessage
     */
    public void saveUserMessage(IMMessage imMessage){
        //保存IM消息
        imMessage.setRead(true);
        messageTimeShow(imMessage);
        messageRepository.saveIMMessage(imMessage);
        //更新窗口消息
        Message message = new Message();
        message.setMessageType(IM_TO_IM);
        message.setMessageId(imMessage.getToAuthorId());
        message.setUserId(getUserId());
        message.setRead(imMessage.isRead());
        message.setMessageCreateTime(imMessage.getMessageCreateTime());
        message.setJsonData(JSONUtils.bean2JsonString(imMessage));
        messageRepository.localSave(message);
    }

    private void messageTimeShow(IMMessage imMessage) {
        Date latestMessageTime = messageRepository.getLatestIMMessageTime();
        if(latestMessageTime!=null&&((imMessage.getMessageCreateTime().getTime()-latestMessageTime.getTime())< TIME_THRESHOLDE)){
            imMessage.setShowTime(false);
        }
    }

    /**
     * 消息类型处理,通知发送
     * @param message
     */
    private  void sendNotifycation(Message message) {
        int messageType = message.getMessageType();
        Class clazz = MessageParser.registers.get(messageType);
        String messageTitle = "";
        String messageContent = "";
        if (clazz != null) {
            message.setData(JSONUtils.jsonString2Bean(message.getJsonData(), clazz));
            Object data = message.getData();
            if (data instanceof FeedPraiseMessage) {
                messageTitle = "点赞消息";
                messageContent = ((FeedPraiseMessage) data).getUserName() + "赞了你的帖子" + ((FeedPraiseMessage) data).getPostTitle();
            } else if (data instanceof CommentMessage) {
                if (messageType == COMMENT_TO_COMMENT) {
                    messageTitle = "评论回复消息";
                    messageContent = ((CommentMessage) data).getFromAuthorName() + " 回复了你的评论:" + ((CommentMessage) data).getContent();
                } else if (messageType == COMMENT_TO_POST) {
                    messageTitle = "评论消息";
                    messageContent = ((CommentMessage) data).getFromAuthorName() + "评论了你的帖子《" + ((CommentMessage) data).getPostTitle() + ": " + ((CommentMessage) data).getContent();
                }
            }else if(data instanceof IMMessage){
                //如果当前不处于im聊天界面，那么发送通知提醒用户
                if(!imSate){
                   messageTitle = ((IMMessage) data).getFromAuthorName() ;
                   messageContent = ((IMMessage) data).getContent();
                }
            }
        } else {
            Timber.e("消息类型" + messageType + "未注册!");
        }
        //发送通知栏消息
        if (!TextUtils.isEmpty(messageTitle) || !TextUtils.isEmpty(messageContent)) {
            MessageNotifycation.getInstance(context).sendNotifycationMessage(context, messageTitle, messageContent);
        }
    }

    /**
     * 登陆后检测绑定
     *
     * @param userLoginState
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(UserLoginState userLoginState) {
        if (userLoginState.isLogin()) {
            String verifyClientId = SPUtils.getValueString(context, WAIT_TO_VERIRFY_CLIENT_ID);
            if (!TextUtils.isEmpty(verifyClientId)) {
                PushInfor pushInfor = new PushInfor();
                pushInfor.setClientId(verifyClientId);
                bindClienId(pushInfor);
            }
            SPUtils.saveKeyValueString(context, WAIT_TO_VERIRFY_CLIENT_ID, "");
        }
    }

    /**
     * 用户id与clientId的绑定更新
     * 当用户处于登陆状态，且第三方推送服务的clientid下发正常进行比较
     * 比较的原则是如果原先本地的用户messagepush:userid=clientId键值对中，clientId
     * 与服务器不一样，那么视为进行了更新，有换手机和换账号两种情况，换手机会导致clientid发生变化，会重新
     * 进行绑定，换账号会导致本地的为空，不会进行更新绑定
     *
     * @param pushInfor
     */
    private void bindClienId(PushInfor pushInfor) {
        if (userMediator.isLoginState(this, false)) {
            if (!TextUtils.isEmpty(pushInfor.getClientId())) {
                if (messageRepository != null) {
                    messageRepository.messageBind(userMediator.getUserId(), pushInfor.getClientId()).subscribeOn(Schedulers.io()).subscribe(jsonResponseResponse -> {
                        if (jsonResponseResponse.isSuccessful() && jsonResponseResponse.body() != null && jsonResponseResponse.body().isOK()) {
                            Timber.d("更新用户id与clientId绑定成功");
                            SPUtils.saveKeyValueString(context, "messagepush:" + userMediator.getUserId(), pushInfor.getClientId());
                        } else {
                            Timber.d("更新用户id与clientId绑定失败");
                        }
                    });
                }
            } else {
                Timber.d("用户id与消息推送clientId一致");
            }
        } else {
            //未登录状态，暂存clientId以便下次刷新
            SPUtils.saveKeyValueString(context, WAIT_TO_VERIRFY_CLIENT_ID, pushInfor.getClientId());
        }
    }


    /**
     * 以明天为时间线，向下搜索本地消息
     */
    public LiveData<DataResource<List<Message>>> observerMessageDbDataChange() {
        Date tommorrow = TimeOffSetUtils.tommorrow();
        return messageRepository.observerMessageDbDataChange(userMediator.getUserId(), tommorrow.getTime(), SETP_PAGE_SIZE);
    }


    /**
     * 获取最小时间基线
     *
     * @param messages
     * @return
     */
    public Date getMinTimeLineBy(List<Message> messages) {
        if (messages == null || messages.size() == 0) {
            //离当前时间最近的数据
            return new Date();
        } else {
            Date timeLine = messages.get(messages.size() - 1).getMessageCreateTime();
            return timeLine;
        }
    }



    public void transformData(List<Message> messages, CallBack<List<Message>> callBack) {
        appExecutors.diskIO().execute(() -> {
            messageCount = 0;
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                //排除掉赞消息的未读情况
                if (!messages.get(i).isRead() && messages.get(i).getMessageType()!=PRAISE_TO_POST) {
                    messageCount++;
                }
                parseMessage(message);
            }
            if (callBack != null) {
                callBack.apply(messages);
                //消息未读检测
                messageCountNotify(messageCount);
            }
        });
    }

    /**
     * 消息刷新
     *
     * @param messageCount
     */
    private void messageCountNotify(int messageCount) {
        Intent intent = new Intent("com.sire.messagecount");
        intent.putExtra("messageCount", messageCount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    private void parseMessage(Message message) {
        int messageType = message.getMessageType();
        Class clazz = MessageParser.registers.get(messageType);
        if (clazz != null) {
            message.setData(JSONUtils.jsonString2Bean(message.getJsonData(), clazz));
        } else {
            Timber.e("消息类型" + messageType + "未注册!");
        }
    }

    public void setMessageRead(Message message) {
        appExecutors.diskIO().execute(() -> {
            messageRepository.setMessageRead(message);
        });
    }


    /**
     * 修改消息未读状态为已读
     *
     * @param message
     */
    public void changeMessageState( Message message) {
        if (!message.isRead()) {
            message.setRead(true);
            //更新数据库已读状态
            setMessageRead(message);
            messageCount--;
            messageCountNotify(messageCount);
        }
    }

    public String getUserId() {
        return userMediator.getUserId();
    }

    public LiveData<DataResource<List<IMMessage>>> observerIMMessageDbDataChange(String talkUserId,Date timeLine,int pageSize) {

       return messageRepository.observerIMMessageDbDataChange(getUserId(),talkUserId,timeLine, pageSize);
    }

    public LiveData<DataResource<IMMessage>> sendMessage(String message, String talkUserID, String talkUserName, String talkUserImg) {
        IMMessage imMessage = new IMMessage();
        imMessage.setMessageId(UUIDUtils.generateSingleId());
        imMessage.setMessageCreateTime(new Date());
        imMessage.setContent(message);
        imMessage.setFromAuthorId(getUserId());
        imMessage.setFromAuthorName(userMediator.getUserName());
        imMessage.setFromAuthorImg(userMediator.getUserImage());
        imMessage.setRead(false);
        imMessage.setToAuthorId(talkUserID);
        imMessage.setToAuthorName(talkUserName);
        imMessage.setToAuthorImg(talkUserImg);
       return messageRepository.sendMessage(imMessage);
    }


    public LiveData<DataResource<Integer>> observerIMMessageCount(String fromAuthorId) {
        return messageRepository.observerIMMessageCount(fromAuthorId);
    }

    public void setIMMessageRead(IMMessage item) {
        appExecutors.diskIO().execute(() -> messageRepository.setIMMessageRead(item));
    }

    public void updateMessageState(int imMessageUnreadCount, Message item) {
        if(imMessageUnreadCount>0){
            if(item.isRead()){
                item.setRead(false);
               setMessageRead(item);
            }
            messageCount = messageCount+imMessageUnreadCount-1;
        }else {
            if(!item.isRead()){
                item.setRead(true);
                setMessageRead(item);
            }
            messageCount = messageCount-imMessageUnreadCount;
        }
        messageCountNotify(messageCount);
    }
}
