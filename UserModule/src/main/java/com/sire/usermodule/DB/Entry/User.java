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
 * Description: private String college;
    private String hobby;
    private String homePageImg;
    private String instruction;
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
    private String college;
    private String hobby;
    private String homePageImg;
    private String instruction;
    private int level;
    private Date loginTime;

    @Ignore
    public User(String userId, String name, String pwd,Date loginTime) {
        this.userId = userId;
        this.name = name;
        this.pwd = pwd;
        this.loginTime = loginTime;
    }
    //以下为非数据库字段
    @Ignore
    private String followingCount;
    @Ignore
    private String followoerCount;
    @Ignore
    private String token;

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getFollowoerCount() {
        return followoerCount;
    }

    public void setFollowoerCount(String followoerCount) {
        this.followoerCount = followoerCount;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getHomePageImg() {
        return homePageImg;
    }

    public void setHomePageImg(String homePageImg) {
        this.homePageImg = homePageImg;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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
                ", college='" + college + '\'' +
                ", hobby='" + hobby + '\'' +
                ", homePageImg='" + homePageImg + '\'' +
                ", instruction='" + instruction + '\'' +
                ", level=" + level +
                ", loginTime=" + loginTime +
                ", token='" + token + '\'' +
                '}';
    }
}
