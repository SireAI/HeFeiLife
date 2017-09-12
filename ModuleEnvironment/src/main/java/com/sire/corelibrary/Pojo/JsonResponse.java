package com.sire.corelibrary.Pojo;


import java.io.Serializable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/8/29
 * Author:Sire
 * Description:
 * ==================================================
 */
public class JsonResponse<T>  {
    private int code;
    private T data;

    public JsonResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public boolean isOK(){
        return code == 200 && data != null;
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
                ", data='" + data + '\'' +
                '}';
    }


}
