package com.sire.bbsmodule.Utils;

import android.content.Context;

import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISCameraConfig;

import static com.sire.corelibrary.Utils.PhotoPickUtils.CODE_CAMERA;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ImagePickerUtils {
    public static final int REQUEST_CAMERA_CODE = 105;
    public static void takePicture(Context context){
        ISCameraConfig config = new ISCameraConfig.Builder().build();
        ISNav.getInstance().toCameraActivity(context, config, REQUEST_CAMERA_CODE);
    }
}
