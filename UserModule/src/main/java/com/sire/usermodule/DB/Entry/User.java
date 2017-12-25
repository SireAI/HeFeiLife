package com.sire.usermodule.DB.Entry;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = "userId")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @NonNull
    private String userId;
    private String name;
    private String pwd;
    private String avatar;
    private String sex;
    private String birthday;
    private String phonenumber;
    private int level;
    private boolean alreadyLogin;
    private Date loginTime;
    @Ignore
    private String token;
    @Ignore
    public User(String userId, String name, String pwd,Date loginTime) {
        this.userId = userId;
        this.name = name;
        this.pwd = pwd;
        this.loginTime = loginTime;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User() {
    }

    public boolean isAlreadyLogin() {
        return alreadyLogin;
    }

    public void setAlreadyLogin(boolean alreadyLogin) {
        this.alreadyLogin = alreadyLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", level=" + level +
                ", alreadyLogin=" + alreadyLogin +
                ", loginTime=" + loginTime +
                ", token='" + token + '\'' +
                '}';
    }
}
