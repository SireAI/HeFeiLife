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
//        String TEST_URL = "http:192.168.43.240:8080";
        String TEST_URL = "http://10.0.2.2:8080";
//        String TEST_URL = "http://localhost:8080/";
        String PRODUCT_URL = "http://182.254.141.44/footprint/";

        String TEST_FILE_URL = "http://localhost:8080/";
        String PRODUCT_FILE_URL = "http://115.159.45.49/storage/";
    }

    public enum APPEnvironment {
        PRODUCT,TEST
    }
    private static APPEnvironment state = APPEnvironment.PRODUCT;

    public static void setState(APPEnvironment state) {
        WebUrl.state = state;
    }

    public static String getHostUrl(){
        return state == APPEnvironment.TEST ? UrlConstant.TEST_URL : UrlConstant.PRODUCT_URL;
    }

    public static String getFileStorageUrl(){
        return state == APPEnvironment.TEST ? UrlConstant.TEST_FILE_URL : UrlConstant.PRODUCT_FILE_URL;
    }
}
