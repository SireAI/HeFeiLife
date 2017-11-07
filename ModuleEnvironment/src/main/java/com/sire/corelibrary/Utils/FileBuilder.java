package com.sire.corelibrary.Utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

import timber.log.Timber;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/9/7
 * Author:sire
 * Description:android文件构建类，应用目录下的文件只能应用自身去访问，外界程序无法访问；
 * SD卡等外部存储空间可以
 * ==================================================
 */
public class FileBuilder {


    /**
     * 文件分类目录
     */
    private String fileTypeDirectoryName = Environment.DIRECTORY_DOWNLOADS;
    /**
     * 数据文件目录/缓存目录（注：缓存目录在系统存储空间吃紧的时候会被删除掉；
     * 应用信息页面删除数据会删除应用内部外部存储目录下的所有数据，
     * 删除缓存会删除应用内部外部存储目录下所有的缓存数据），只有sd等外部存储设备上的文件是共享，
     * 可以任意访问，但是应用程序目录下的文件只有应用程序自身能访问。
     */
    private FileType fileType = FileType.DATA;
    /**
     * this final file name
     */
    private String fileName;

    private FileBuilder() {
    }

    public static FileBuilder create() {
        return new FileBuilder();
    }

    public FileBuilder withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }



    public FileBuilder withfileTypeDirectoryName(String fileTypeDirectoryName) {
        this.fileTypeDirectoryName = fileTypeDirectoryName;
        return this;

    }

    public FileBuilder withFileType(FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public File build(Context context) {
        StringBuilder sb = new StringBuilder();
            if (isSDCardAvailable()) {
                sdcardFile(context, sb);
            } else {
                appFile(context, sb);
            }
        return checkFile(sb);
    }

    @Nullable
    private File checkFile(StringBuilder sb) {
        if (TextUtils.isEmpty(fileName)) {
            throw new RuntimeException("文件名不能为空");
        }
        boolean makeDirSuccess = createDirs(sb.toString());
        if (!makeDirSuccess) {
            Timber.e(sb.toString() + "文件目录创建失败");
            return null;
        } else {
            sb.append(File.separator);
            sb.append(fileName);
            return new File(sb.toString());
        }
    }

    private void appFile(Context context, StringBuilder sb) {
        if (fileType == FileType.DATA) {
            sb.append(context.getFilesDir().getAbsolutePath());
            sb.append(File.separator);
            sb.append(fileTypeDirectoryName);
        } else {
            sb.append(context.getCacheDir().getAbsolutePath());
            sb.append(File.separator);
            sb.append(fileTypeDirectoryName);
        }
    }


    private void sdcardFile(Context context, StringBuilder sb) {
        if (fileType == FileType.DATA) {
            sb.append(context.getExternalFilesDir(fileTypeDirectoryName).getAbsolutePath());
        } else {
            sb.append(context.getExternalCacheDir().getAbsolutePath());
            sb.append(File.separator);
            sb.append(fileTypeDirectoryName);
        }
    }

    /**
     * 创建文件夹
     */
    private boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 检查SD卡是否可用于读写
     */
    private boolean isSDCardAvailable() {

        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }


    /**
     * 文件名
     */

    public enum FileType {
        DATA, CACHE
    }


}