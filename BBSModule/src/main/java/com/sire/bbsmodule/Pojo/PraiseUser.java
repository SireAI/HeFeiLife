package com.sire.bbsmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class PraiseUser {
    private List<PraiseInfor> feedPraises;

    private int praiseCount;

    public List<PraiseInfor> getFeedPraises() {
        return feedPraises;
    }

    public void setFeedPraises(List<PraiseInfor> feedPraises) {
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
        return "PraiseUser{" +
                "feedPraises=" + feedPraises +
                ", praiseCount=" + praiseCount +
                '}';
    }
}
