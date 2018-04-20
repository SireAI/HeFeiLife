package com.sire.feedmodule.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/20
 * Author:Sire
 * Description:
 * ==================================================
 */

public class StringUtils {
    public static String takeFristText(String content){
        if(content.contains("<h4>") && content.contains("</h4>")){
            int endIndex = content.indexOf("<img src=");
            if(endIndex==-1){
                endIndex = content.length();
            }
            return content.substring(content.indexOf("</h4>")+5, endIndex);
        }else {
            return content;
        }
    }
}
