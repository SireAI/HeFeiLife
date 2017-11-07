package com.sire.corelibrary.Networking;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/20
 * Author:Sire
 * Description:
 * ==================================================
 */
public class WebUrl {
    public interface UrlConstant{
        String TEST_URL = "http://10.0.2.2:8080";
//        String TEST_URL = "http://localhost:8080";
        String PRODUCT_URL = "http://gank.io/api";
    }

    enum Envionment{
        PRODUCT,TEST
    }
    private static Envionment state = Envionment.TEST;

    public static String getHostUrl(){
        return state == Envionment.TEST ? UrlConstant.TEST_URL : UrlConstant.PRODUCT_URL;
    }
}
