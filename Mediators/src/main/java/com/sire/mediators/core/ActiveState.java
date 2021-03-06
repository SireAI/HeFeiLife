package com.sire.mediators.core;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public enum ActiveState {
    SUCCESS("成功"),FAILED("失败");

    private String message ;

    ActiveState(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ActiveState{" +
                "message='" + message + '\'' +
                '}';
    }
}

