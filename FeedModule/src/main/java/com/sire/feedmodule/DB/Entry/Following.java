package com.sire.feedmodule.DB.Entry;


import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/25
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = {"userId","followingId"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Following implements Serializable {
    @NonNull
    private String userId;
    @NonNull
    private String followingId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowingId() {
        return followingId;
    }

    public void setFollowingId(String followingId) {
        this.followingId = followingId;
    }

    @Override
    public String toString() {
        return "Following{" +
                "userId='" + userId + '\'' +
                ", followingId='" + followingId + '\'' +
                '}';
    }
}