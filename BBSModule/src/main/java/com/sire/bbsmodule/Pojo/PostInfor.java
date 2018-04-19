package com.sire.bbsmodule.Pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostInfor implements Serializable{

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
     * 用户头像地址
     */
    private String authorIcon;
    /**
     * 分类
     */
    private String categary;
    /**
     * 赞的数目
     */
    private int praiseCount;

    @Ignore
    private String[] splitPictures;
    /**
     * 精确到毫秒级
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss SSS")
    private Date timeLine;
    /**
     * feed时间，这个时间更新创建和更新时间
     */
    /**
     * 用户等级
     */
    private String authorLevel;
    /**
     * 发表地点
     */
    private String publishAddress;

    private boolean isFollow ;

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        this.isFollow = follow;
    }

    public String getAuthorLevel() {
        return authorLevel;
    }

    public void setAuthorLevel(String authorLevel) {
        this.authorLevel = authorLevel;
    }

    public String getPublishAddress() {
        return publishAddress;
    }

    public void setPublishAddress(String publishAddress) {
        this.publishAddress = publishAddress;
    }

    /**
     * 将图片Url分割到数据集合中
     *
     * @param pictureUrls
     */
    private void splitUrls(String pictureUrls) {
        this.splitPictures = new String[]{};
        if (!TextUtils.isEmpty(pictureUrls)) {
            splitPictures = pictureUrls.split(",");
        }
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String[] getSplitPictures() {
        return splitPictures;
    }



    public String getCategary() {
        return categary;
    }

    public void setCategary(String categary) {
        this.categary = categary;
    }

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
        splitUrls(pictureUrls);
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
        return "PostInfor{" +
                "authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", feedId='" + feedId + '\'' +
                ", feedType=" + feedType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pictureUrls='" + pictureUrls + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", authorIcon='" + authorIcon + '\'' +
                ", categary='" + categary + '\'' +
                ", praiseCount=" + praiseCount +
                ", splitPictures=" + Arrays.toString(splitPictures) +
                ", timeLine=" + timeLine +
                ", authorLevel='" + authorLevel + '\'' +
                ", publishAddress='" + publishAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        PostInfor feedInfor = (PostInfor) o;

        return feedId.equals(feedInfor.feedId) && authorId.equals(feedInfor.authorId);
    }


}
