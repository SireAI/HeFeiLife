package com.sire.feedmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;

import java.util.Date;
public class FeedRequestInfor {
    private String userId;
    private int pageSize;
    private int pageIndex;
    private int pageOffset;
    private String feedType;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss SSS")
    private Date timeLine;
    public boolean getNewFeeds;
    @JsonIgnore
    private DataSourceStrategy.DataFromStrategy dataFromStrategy;
    public DataSourceStrategy.DataFromStrategy getDataFromStrategy() {
        return dataFromStrategy;
    }

    public void setDataFromStrategy(DataSourceStrategy.DataFromStrategy dataFromStrategy) {
        this.dataFromStrategy = dataFromStrategy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public boolean isGetNewFeeds() {
        return getNewFeeds;
    }

    public void setGetNewFeeds(boolean getNewFeeds) {
        this.getNewFeeds = getNewFeeds;
    }

    public Date getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(Date timeLine) {
        this.timeLine = timeLine;
    }

    @Override
    public String toString() {
        return "FeedRequestInfor{" +
                "userId='" + userId + '\'' +
                ", pageSize=" + pageSize +
                ", pageIndex=" + pageIndex +
                ", pageOffset=" + pageOffset +
                ", feedType='" + feedType + '\'' +
                ", timeLine=" + timeLine +
                ", getNewFeeds=" + getNewFeeds +
                ", dataFromStrategy=" + dataFromStrategy +
                '}';
    }
}