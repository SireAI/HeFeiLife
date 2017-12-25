package com.sire.bbsmodule.Pojo;

import android.text.TextUtils;

public class EditData {
    public EditData(String inputStr, String imagePath) {
        this.inputStr = inputStr;
        this.imagePath = imagePath;
    }

    public EditData() {
    }

    public String inputStr;
    public String imagePath;

    public boolean isEmpty(){
        return TextUtils.isEmpty(inputStr)&&TextUtils.isEmpty(imagePath);
    }
}