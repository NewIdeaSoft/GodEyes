package com.nisoft.inspectortools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/5/19.
 */

public class FileUtil {
    /***
     *  复制源文件到目标文件，删除源文件
     * @param resPath 源文件路径
     * @param targetPath 目标路径
     */
    public static void moveFile(String resPath,String targetPath){
        try {
            File file = new File(resPath);
            File targetFile = new File(targetPath);
            if (targetFile.exists()){
                targetFile.delete();
            }

            if (file.exists()){
                int byteSum = 0;
                int byteRead = 0;
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(targetPath);
                byte[] buffer = new byte[1024];
                while((byteRead = in.read(buffer))!=-1){
                    byteSum += byteRead;
                    out.write(buffer,0,byteRead);
                }

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeStringToFile(String data,String filePath){
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
        FileWriter writer;
        try {
            file.createNewFile();
            writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openImageFile(String path, Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件的MimeType
        File file = new File(path);
        String fileExpandedName = getFileExpandedName(file.getName());
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExpandedName);
        if (type != null) {
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        }
    }
    public static String getFileExpandedName(String name) {
        String expandedName = "";
        int index = -1;
        index = name.lastIndexOf(".");
        if (index != -1) {
            expandedName = name.substring(index + 1, name.length()).toLowerCase();
        }
        return expandedName;
    }
}
