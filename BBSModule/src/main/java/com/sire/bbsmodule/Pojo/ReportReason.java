package com.sire.bbsmodule.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/08
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportReason implements Serializable{
    /**
     * 0表示评论，1表示feed，2表示个人
     */
    private int type;
    private String id;
    private String content;
    private String reportReason;

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ReportReason{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", reportReason='" + reportReason + '\'' +
                '}';
    }
}
