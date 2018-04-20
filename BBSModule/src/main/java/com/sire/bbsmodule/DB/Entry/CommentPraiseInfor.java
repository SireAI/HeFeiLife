package com.sire.bbsmodule.DB.Entry;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/28
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = {"feedId","commentId"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentPraiseInfor {
    @NonNull
    private String feedId;
    @NonNull
    private String commentId;



    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        return "CommentPraiseInfor{" +
                ", feedId='" + feedId + '\'' +
                ", commentId='" + commentId + '\'' +
                '}';
    }
}
