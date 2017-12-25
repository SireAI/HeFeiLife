package com.sire.feedmodule.Pojo;



/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/28
 * Author:Sire
 * Description:
 * ==================================================
 */
public class FeedPraiseUserRequestInfor {
    private String feedId;
    private int pageIndex ;
    private int pageSize;
    private int pageOffset;

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
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

    @Override
    public String toString() {
        return "FeedPraiseUserRequestInfor{" +
                "feedId='" + feedId + '\'' +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", pageOffset=" + pageOffset +
                '}';
    }
}
