package com.sire.messagepushmodule.Utils;


import com.sire.messagepushmodule.DB.Entry.IMMessage;
import com.sire.messagepushmodule.Pojo.CommentMessage;
import com.sire.messagepushmodule.Pojo.FeedPraiseMessage;

import java.util.HashMap;
import java.util.Map;

import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_COMMENT;
import static com.sire.messagepushmodule.Constant.Constant.COMMENT_TO_POST;
import static com.sire.messagepushmodule.Constant.Constant.IM_TO_IM;
import static com.sire.messagepushmodule.Constant.Constant.PRAISE_TO_POST;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public class MessageParser {
    public static final String MESSAGE_TYPE = "messageType";
    public static Map<Integer, Class> registers = new HashMap<>();

    static {
        registers.put(COMMENT_TO_COMMENT, CommentMessage.class);
        registers.put(COMMENT_TO_POST, CommentMessage.class);
        registers.put(IM_TO_IM, IMMessage.class);
        registers.put(PRAISE_TO_POST, FeedPraiseMessage.class);
//        registers.put(SYSTEM_NOTIFYCATION, CommentMessage.class);
    }

//    public static ClientMessage parse(String message) {
//        ClientMessage clientMessage = null;
//        try {
//            JSONObject jsonObject = new JSONObject(message);
//            int messageType = jsonObject.getInt(MESSAGE_TYPE);
//            TypeReference<ClientMessage> typeReference = registers.get(messageType);
//            if (typeReference != null) {
//                clientMessage = new ClientMessage<CommentMessage>();
//                clientMessage = JSONUtils.jsonString2Bean(message, typeReference);
//                if(clientMessage!=null && clientMessage.getData()!=null){
//                    clientMessage.setJsonData(jsonObject.getString("data"));
//                }
//            } else {
//                Timber.e("解析类型未注册!");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return clientMessage;
//    }

}
