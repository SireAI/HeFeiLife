package com.sire.bbsmodule.Pojo;

import android.text.TextUtils;

import java.io.File;

public class EditData {
    public EditData(String inputStr, String imagePath) {
        this.inputStr = inputStr;
        this.imagePath = imagePath;
    }

    public EditData() {
    }

    public String inputStr;
    public String imagePath;
    public String imageWidth;
    public String imageHeight;

    public boolean isEmpty(){
        return TextUtils.isEmpty(inputStr)&&TextUtils.isEmpty(imagePath);
    }
    public boolean isImage(){
        return !TextUtils.isEmpty(imagePath) && new File(imagePath).exists();
    }
    public boolean isNormalText(){
        return !TextUtils.isEmpty(inputStr) ;
    }
}