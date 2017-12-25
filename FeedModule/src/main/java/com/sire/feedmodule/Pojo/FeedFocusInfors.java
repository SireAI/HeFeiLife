package com.sire.feedmodule.Pojo;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/01
 * Author:Sire
 * Description:
 * ==================================================
 */

public class FeedFocusInfors {
    private boolean isFollow;
    private boolean isPraise;

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    @Override
    public String toString() {
        return "FeedFocusInfors{" +
                "isFollow=" + isFollow +
                ", isPraise=" + isPraise +
                '}';
    }
}
