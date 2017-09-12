package com.sire.mediators.UserModuleInterface;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class UserInfor {
    private String name;
    private String pwd;

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

    @Override
    public String toString() {
        return "UserInfor{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
