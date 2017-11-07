package com.sire.corelibrary.Networking.downlaod.downloadInfor;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/4
 * Author:sire
 * Description:下载信息
 * ==================================================
 */

@Entity
public class DownloadFileInfor {
    @PrimaryKey
    private long id;
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long contentLength;
    private long lengthPerRead;
    /*已下载长度*/
    private long readStartPonint;
    /**
     * -1 means no broken inofor
     */
    @Ignore
    private long tempBrokenPosition = -1;
    /*超时设置*/
    private int connectonTime = 6;
    /*state状态数据库保存*/
    private int stateInte;
    /*url*/
    private String url;

    @Ignore
    public DownloadFileInfor(String url) {
        setUrl(url);
    }

    @Ignore
    public DownloadFileInfor(long id, String savePath, long countLength, long readStartPonint,
                             int connectonTime, int stateInte, String url) {
        this.id = id;
        this.savePath = savePath;
        this.contentLength = countLength;
        this.readStartPonint = readStartPonint;
        this.connectonTime = connectonTime;
        this.stateInte = stateInte;
        this.url = url;
    }

    public DownloadFileInfor() {
    }

    public long getLengthPerRead() {
        return lengthPerRead;
    }

    public void setLengthPerRead(long lengthPerRead) {
        this.lengthPerRead = lengthPerRead;
    }

    public long getTempBrokenPosition() {
        return tempBrokenPosition;
    }

    public void setTempBrokenPosition(long tempBrokenPosition) {
        this.tempBrokenPosition = tempBrokenPosition;
    }

    public DownState getState() {
        switch (getStateInte()) {
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }

    @Override
    public String toString() {
        return "DownloadFileInfor{" +
                "id=" + id +
                ", savePath='" + savePath + '\'' +
                ", contentLength=" + contentLength +
                ", lengthPerRead=" + lengthPerRead +
                ", readStartPonint=" + readStartPonint +
                ", tempBrokenPosition=" + tempBrokenPosition +
                ", connectonTime=" + connectonTime +
                ", stateInte=" + stateInte +
                ", url='" + url + '\'' +
                '}';
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }


    public long getReadStartPonint() {
        return readStartPonint;
    }

    public void setReadStartPonint(long readStartPonint) {
        this.readStartPonint = readStartPonint;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public void copy(DownloadFileInfor downloadFileInfor) {
        this.setId(downloadFileInfor.getId());
        this.setReadStartPonint(downloadFileInfor.getReadStartPonint());
        this.setContentLength(downloadFileInfor.getContentLength());
        this.setConnectonTime(downloadFileInfor.getConnectonTime());
        this.setLengthPerRead(downloadFileInfor.getLengthPerRead());
        this.setSavePath(downloadFileInfor.getSavePath());
        this.setState(downloadFileInfor.getState());
        this.setStateInte(downloadFileInfor.getStateInte());
        this.setUrl(downloadFileInfor.getUrl());
    }
}
