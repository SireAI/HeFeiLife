package com.sire.bbsmodule.Pojo;


import android.arch.persistence.room.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/1
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PraiseInfor {
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
        return super.toString();
    }
}
