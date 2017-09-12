package com.sire.corelibrary.Lifecycle.ModuleLife;

import com.sire.mediators.core.Module;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class APPInitService {
    private static Map<String,Object> services = new HashMap<>();
    public static <T> void registerService(Module module,T service){
        Object cacheService = services.get(module.name());
        if(cacheService==null){
            services.put(module.name(),service);
            System.out.println("APPService:"+module.name()+"服务注册成功");
        }else {
            System.out.println("APPService:"+module.name()+"服务重复注册!");
        }
    }
    public static <T> T getModuleInitServiceFrom(Module module){
        T service = (T) services.get("APPService:"+module.name());
        if(service == null){
            System.out.println("APPService:"+module.name()+"服务未注册！");
        }
        return service;
    }
}
