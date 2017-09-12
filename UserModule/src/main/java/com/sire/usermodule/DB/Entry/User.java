package com.sire.usermodule.DB.Entry;

import android.arch.persistence.room.Entity;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@Entity(primaryKeys = "id")
public class User {
    private String id;
    private String name;
    private String pwd;
    private boolean alreadyLogin;

    public User(String id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public boolean isAlreadyLogin() {
        return alreadyLogin;
    }

    public void setAlreadyLogin(boolean alreadyLogin) {
        this.alreadyLogin = alreadyLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        return pwd.equals(user.pwd);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + pwd.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", alreadyLogin=" + alreadyLogin +
                '}';
    }
}
