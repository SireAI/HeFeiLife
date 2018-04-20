package com.sire.corelibrary.Networking.Response;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/13
 * Author:Sire
 * Description:
 * ==================================================
 */

public  class JsonResponse<T> {
    private int code;
    private T data;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

    public boolean isOK() {
        return code == 200;
    }
}
