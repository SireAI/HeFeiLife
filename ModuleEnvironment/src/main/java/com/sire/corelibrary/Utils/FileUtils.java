package com.sire.corelibrary.Utils;

import java.io.File;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class FileUtils {
    public static void deleteFile(File file,boolean keepDirectory) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f,keepDirectory);
            }
            if(keepDirectory){
                file.delete();//如要保留文件夹，只删除文件，请注释这行
            }
        } else if (file.exists()) {
            file.delete();
        }
    }
}
