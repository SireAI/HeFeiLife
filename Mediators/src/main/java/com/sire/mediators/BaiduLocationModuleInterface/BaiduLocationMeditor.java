package com.sire.mediators.BaiduLocationModuleInterface;

import com.sire.mediators.core.CallBack;

import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/02
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface BaiduLocationMeditor {

    /**
     * 定位注册方法
     * @param clazz  activity的class对象
     * @param callBack   定位结果回调
     */
    void registerCommponnent(Class clazz, CallBack<Map<String,Object>> callBack);

    /**
     * 定位开启与关闭
     * @param locate  true表示开启定位，false表示关闭定位
     */
    void locate(boolean locate);
}
