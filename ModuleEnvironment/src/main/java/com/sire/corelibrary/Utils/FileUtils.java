package com.sire.corelibrary.Utils;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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

    public static void bytesToFile(byte[] buffer, final String filePath){

        File file = new File(filePath);

        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;

        try {
            output = new FileOutputStream(file);

            bufferedOutput = new BufferedOutputStream(output);

            bufferedOutput.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(null!=bufferedOutput){
                try {
                    bufferedOutput.flush();
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(null != output){
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static synchronized Object readObjectFromFile(Context context, String fileName) {
        File file = null;
        Object object = null;
        try {
            file = FileBuilder
                    .create()
                    .withFileType(FileBuilder.FileType.DATA)
                    .withfileTypeDirectoryName("Object")
                    .withFileName(fileName).build(context);
            if (!file.exists()) {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            object = objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static synchronized void objectToFile(Context context,String fileName, Object object) {
        File file = null;
        try {
            file = FileBuilder
                    .create()
                    .withFileType(FileBuilder.FileType.DATA)
                    .withfileTypeDirectoryName("Object")
                    .withFileName(fileName).build(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
