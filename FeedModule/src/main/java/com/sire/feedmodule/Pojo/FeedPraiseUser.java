package com.sire.feedmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sire.feedmodule.DB.Entry.FeedPraiseInfor;

import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/06
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedPraiseUser {
    private List<FeedPraiseInfor> feedPraises;

    private int praiseCount;

    public List<FeedPraiseInfor> getFeedPraises() {
        return feedPraises;
    }

    public void setFeedPraises(List<FeedPraiseInfor> feedPraises) {
        this.feedPraises = feedPraises;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    @Override
    public String toString() {
        return "FeedUserPraiseInfor{" +
                "feedPraises=" + feedPraises +
                ", praiseCount=" + praiseCount +
                '}';
    }
}
