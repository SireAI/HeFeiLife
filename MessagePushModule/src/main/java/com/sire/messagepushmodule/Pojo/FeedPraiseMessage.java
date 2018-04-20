package com.sire.messagepushmodule.Pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/1
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedPraiseMessage {
    /**
     * feed标示
     */
    private String feedId;

    /**
     * 帖子作者id
     */
    private String postAuthorId;

    private String postTitle;
    /**
     * 点赞此feed的用户标示
     */
    private String userId;
    /**
     * 用户头像
     */
    private String userImage;
    /**
     * 点赞时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss SSS")
    private Date praiseTime;
    /**
     * 用户姓名
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getPostAuthorId() {
        return postAuthorId;
    }

    public void setPostAuthorId(String postAuthorId) {
        this.postAuthorId = postAuthorId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Date getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(Date praiseTime) {
        this.praiseTime = praiseTime;
    }

    @Override
    public String toString() {
        return "FeedPraiseMessage{" +
                "feedId='" + feedId + '\'' +
                ", postAuthorId='" + postAuthorId + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", userId='" + userId + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
