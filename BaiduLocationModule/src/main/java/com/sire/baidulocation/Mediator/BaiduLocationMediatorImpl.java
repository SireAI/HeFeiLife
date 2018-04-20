package com.sire.baidulocation.Mediator;

import com.sire.baidulocation.ViewModel.BaiduLocationViewModel;
import com.sire.mediators.BaiduLocationModuleInterface.BaiduLocationMeditor;
import com.sire.mediators.core.CallBack;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/02
 * Author:Sire
 * Description:
 * ==================================================
 */
public class BaiduLocationMediatorImpl implements BaiduLocationMeditor {
    private  BaiduLocationViewModel baiduLocationViewModel;

    @Inject
    public BaiduLocationMediatorImpl(BaiduLocationViewModel baiduLocationViewModel) {
        this.baiduLocationViewModel = baiduLocationViewModel;
    }


    @Override
    public void registerCommponnent(Class clazz, CallBack<Map<String, Object>> callBack) {
        baiduLocationViewModel.registerCommponent(clazz,callBack);
    }

    @Override
    public void locate(boolean locate) {
        baiduLocationViewModel.locate(locate);
    }
}
