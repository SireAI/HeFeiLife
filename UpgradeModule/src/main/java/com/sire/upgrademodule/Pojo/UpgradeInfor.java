package com.sire.upgrademodule.Pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/2
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpgradeInfor implements Serializable{

    private static final long serialVersionUID = -1466673116166012781L;

    private boolean needUpgrade;
    private String  versionName;
    private String upgradeMessage;
    private boolean forceUpgrade;
    private String downloadUrl;
    private long fileSize;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpgradeMessage() {
        return upgradeMessage;
    }

    public void setUpgradeMessage(String upgradeMessage) {
        this.upgradeMessage = upgradeMessage;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "UpgradeInfor{" +
                "needUpgrade=" + needUpgrade +
                ", versionName='" + versionName + '\'' +
                ", upgradeMessage='" + upgradeMessage + '\'' +
                ", forceUpgrade=" + forceUpgrade +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    public boolean isNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        this.needUpgrade = needUpgrade;
    }
}
