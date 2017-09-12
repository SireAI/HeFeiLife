package com.sire.upgrademodule.Pojo;



public class VersionInfo {


    private int versionCode;
    private String versionName;
    private boolean forceUpgrade;
    private String versionMessage;
    private String downloadUrl;


    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionMessage() {
        return versionMessage;
    }

    public void setVersionMessage(String versionMessage) {
        this.versionMessage = versionMessage;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", forceUpgrade=" + forceUpgrade +
                ", versionMessage='" + versionMessage + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
