package com.sire.mediators.core;

import java.lang.ref.SoftReference;


/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class AppExit<T> {
    private SoftReference<T> applicationSoftReference;

    public void setApp(T app) {
        applicationSoftReference = new SoftReference<>(app);
    }

    public T getApplication(){
        if(applicationSoftReference!=null && applicationSoftReference.get()!=null){
            return applicationSoftReference.get();
        }
        return null;
    }
}
