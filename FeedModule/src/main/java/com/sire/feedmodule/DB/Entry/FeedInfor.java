package com.sire.feedmodule.DB.Entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = "feedId")
public class FeedInfor {

    /**
     * 作者id
     */
    private String authorId;

    /**
     * 作者名字
     */
    private String authorName;
    /**
     * feed信息id
     */
    @NonNull
    private String feedId;
    /**
     * feed类型,0表示信息
     */
    private int feedType;

    /**
     * feed 标题
     */
    private String title;
    /**
     * feed 内容
     */
    private String content;
    /**
     * 图片地址，使用','进行分割
     */
    private String pictureUrls;
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 赞的数目
     */
    private int praiseCount;

    /**
     * feed时间，这个时间更新创建和更新时间
     */
    /**
     * 精确到毫秒级
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss SSS")
    private Date timeLine;



    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public Date getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(Date timeLine) {
        this.timeLine = timeLine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(String pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    @Override
    public String toString() {
        return "FeedInfor{" +
                "authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", feedId='" + feedId + '\'' +
                ", feedType=" + feedType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pictureUrls='" + pictureUrls + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", praiseCount=" + praiseCount +
                ", timeLine=" + timeLine +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        FeedInfor feedInfor = (FeedInfor) o;

        return feedId.equals(feedInfor.feedId)&&authorId.equals(feedInfor.authorId);
    }


}
