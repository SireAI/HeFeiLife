package com.sire.messagepushmodule.Pojo;

import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/30
 * Author:Sire
 * Description:
 * ==================================================
 */

public class MessageRequestInfor {
    private Date timeLine;
    private int pageSize;

    public Date getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(Date timeLine) {
        this.timeLine = timeLine;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
