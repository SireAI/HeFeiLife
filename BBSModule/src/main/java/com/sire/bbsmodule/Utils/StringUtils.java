package com.sire.bbsmodule.Utils;

import android.text.TextUtils;

import com.sire.bbsmodule.Pojo.EditData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {
    public static List<EditData> cutStringBy(String text) {
        String[] splits = text.split("<img src=\"");
        EditData titleEditData = new EditData("未设置标题", null);
        List<EditData> datas = new ArrayList<>();
        for (int i = 0; i < splits.length; i++) {
            EditData editData = new EditData();
            String strData = splits[i];
            if (strData.contains("\"/>")) {
                String[] strings = strData.split("\"/>");
                editData.imagePath = strings[0];
                datas.add(editData);
                if (strings.length == 2) {
                    EditData editDataStr = new EditData();
                    editDataStr.inputStr = strings[1];
                    datas.add(editDataStr);
                }
                continue;
            } else if (strData.startsWith("<h4>")) {
                String substring = strData.substring(strData.indexOf("<h4>") + 4, strData.indexOf("</h4>"));
                editData.inputStr = strData.substring(strData.indexOf("</h4>") + 5);
                titleEditData.inputStr = substring;
            } else {
                editData.inputStr = strData;
            }
            if (!editData.isEmpty()) {
                datas.add(editData);
            }
        }
        datas.add(0, titleEditData);
        return datas;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'HF'yyyy:MM:dd:HH:mm:ss");
        return dateFormat.format(date) + ".webp";
    }

    public static Map<String, String> cutUrlParams2Map(String url) {
        String keyValue = url.substring(url.indexOf("?") + 1);
        Map<String, String> params = new LinkedHashMap<>();
        if (!TextUtils.isEmpty(keyValue)) {
            if (!TextUtils.isEmpty(keyValue)) {
                String[] split = keyValue.split("&");
                if (split != null && split.length > 1) {
                    for (int i = 0; i < split.length; i++) {
                        String[] subSplit = split[i].split("=");
                        if (subSplit != null && subSplit.length == 2) {
                            params.put(subSplit[0], subSplit[1]);
                        }
                    }
                }
            }
        }

        return params;
    }

    public static ArrayList<String> locationMapToList(Map<String, Object> locationResult) {
        ArrayList<String> pois = new ArrayList<>();
        for (Map.Entry<String, Object> entry : locationResult.entrySet()) {
            Object value = entry.getValue();
            if(pois.contains(value)){
                continue;
            }
            if (value instanceof String) {
                pois.add((String) value);
            } else if (value instanceof List) {
                pois.addAll((Collection<? extends String>) value);
            }
        }
        return pois;
    }
}