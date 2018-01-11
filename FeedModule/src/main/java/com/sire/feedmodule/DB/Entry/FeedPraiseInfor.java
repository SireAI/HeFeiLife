package com.sire.feedmodule.DB.Entry;


import android.arch.persistence.room.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/1
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = {"userId","feedId"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedPraiseInfor {
    /**
     * feed标示
     */
    private String feedId;
    /**
     * 点赞此feed的用户标示
     */
    private String userId;
    /**
     * 用户头像
     */
    private String userImage;

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

    @Override
    public String toString() {
        return "FeedPraiseInfor{" +
                "feedId='" + feedId + '\'' +
                ", userId='" + userId + '\'' +
                ", userImage='" + userImage + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
