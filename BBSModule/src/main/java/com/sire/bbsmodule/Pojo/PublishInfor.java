package com.sire.bbsmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/21
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublishInfor implements Serializable{
    private String authorId;
    private String authorName;
    private int feedType;
    /**
     * 精确到毫秒级
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss SSS")
    private Date timeLine;
    private String title;
    private String content;
    private List<String> pictureUrls;
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
     * 用户等级
     */
    private String authorLevel;
    /**
     * 发表地点
     */
    private String publishAddress;

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getCategary() {
        return categary;
    }

    public void setCategary(String categary) {
        this.categary = categary;
    }

    @Override
    public String toString() {
        return "PublishInfor{" +
                "authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", feedType=" + feedType +
                ", timeLine=" + timeLine +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pictureUrls=" + pictureUrls +
                ", videoUrl='" + videoUrl + '\'' +
                ", authorIcon='" + authorIcon + '\'' +
                ", categary='" + categary + '\'' +
                ", authorLevel='" + authorLevel + '\'' +
                ", publishAddress='" + publishAddress + '\'' +
                '}';
    }
}
