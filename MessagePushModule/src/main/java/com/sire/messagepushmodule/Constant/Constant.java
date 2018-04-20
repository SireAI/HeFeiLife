package com.sire.messagepushmodule.Constant;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface Constant {
    String WAIT_TO_VERIRFY_CLIENT_ID = "wait_to_verirfy_clientId";

    /**
     * 消息类型,评论的评论
     */
    int COMMENT_TO_COMMENT = 0;
    /**
     * 消息类型,贴子的评论
     */
    int COMMENT_TO_POST = 1;
    /**
     * 消息类型,贴子的赞
     */
    int PRAISE_TO_POST = 2;
    /**
     * 消息类型,评论的赞
     */
    int PRAISE_TO_COMMENT = 3;

    /**
     * 消息类型,系统通知
     */
    int SYSTEM_NOTIFYCATION = 4;

    /**
     * 消息类型，对话消息
     */
    int IM_TO_IM = 5;

    String COMMENT_MESSAGE = "commentMessage";
    String TALK_USER_ID = "talk_userid";
    String TALK_USER_NAME = "talk_user_name";
    String TALK_USER_IMG = "talk_user_img";

}
