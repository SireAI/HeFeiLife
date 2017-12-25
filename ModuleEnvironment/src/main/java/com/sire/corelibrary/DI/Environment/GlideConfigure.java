package com.sire.corelibrary.DI.Environment;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sire.corelibrary.R;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/17
 * Author:Sire
 * Description:
 * ==================================================
 */

public class GlideConfigure {
    public static RequestOptions getConfigure(@NonNull DiskCacheStrategy strategy){
        RequestOptions cropOptions = new RequestOptions()
                .centerCrop()
                .encodeFormat(Bitmap.CompressFormat.WEBP)
                .placeholder(R.drawable.logo_grey)
                .error(R.drawable.logo_grey)
                .diskCacheStrategy(strategy);
        return cropOptions;
    }
}
