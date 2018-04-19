package com.sire.upgrademodule.Pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/2
 * Author:Sire
 * Description:
 * ==================================================
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class APPInfor {

    private String  versionName;
    private int versionCode;



    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "APPInfor{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
