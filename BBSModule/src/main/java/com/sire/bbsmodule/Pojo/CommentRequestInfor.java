package com.sire.bbsmodule.Pojo;


import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/9
 * Author:Sire
 * Description:
 * ==================================================
 */
public class CommentRequestInfor {

    private String feedId;
    private int pageSize;
    private int pageOffset;
    private Date timeLine;

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Date getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(Date timeLine) {
        this.timeLine = timeLine;
    }

    @Override
    public String toString() {
        return "CommentRequestInfor{" +
                "feedId='" + feedId + '\'' +
                ", pageSize=" + pageSize +
                ", pageOffset=" + pageOffset +
                ", timeLine=" + timeLine +
                '}';
    }
}
