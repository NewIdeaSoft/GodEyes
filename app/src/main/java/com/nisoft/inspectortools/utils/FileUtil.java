package com.nisoft.inspectortools.utils;

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
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/19.
 */

public class FileUtil {
    /***
     *  复制源文件到目标文件，删除源文件
     * @param resPath 源文件路径
     * @param targetPath 目标路径
     */
    public static void moveFile(String resPath, String targetPath) {
        try {
            File file = new File(resPath);
            File targetFile = new File(targetPath);
            if (targetFile.exists()) {
                targetFile.delete();
            }

            if (file.exists()) {
                int byteSum = 0;
                int byteRead = 0;
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(targetPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = in.read(buffer)) != -1) {
                    byteSum += byteRead;
                    out.write(buffer, 0, byteRead);
                }

                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeStringToFile(String data, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
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

    public static void openImageFile(String path, Context context) {
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

    public static ArrayList<String> getAllImagesName(String folderPath) {
        ArrayList<String> imagesName = new ArrayList<>();
        File file = new File(folderPath);
        if (!file.exists()) {
            return null;
        }
        String[] picsName = file.list();
        if (picsName == null || picsName.length == 0) {
            return null;
        }
        for (int i = 0; i < picsName.length; i++) {
            String[] strings = picsName[i].split("\\.");
            String type = strings[strings.length - 1];
            if (type.equals("jpg") || type.equals("bmp")) {
                imagesName.add(picsName[i]);
            }
        }
        return imagesName;
    }

    public static ArrayList<String> getAllFilesPath(String localFolder
            , String serviceAddress
            , ArrayList<String> allFilesNameOnServer) {

        ArrayList<String> filesUrl = new ArrayList<>();
        ArrayList<String> localFilesName = getAllImagesName(localFolder);
        if (localFilesName == null) {
            for (String name : allFilesNameOnServer) {
                filesUrl.add(serviceAddress + name);
            }
            return filesUrl;
        }
        for (String name : allFilesNameOnServer) {
            if (localFilesName.indexOf(name) >= 0) {
                filesUrl.add(localFolder + name);
            } else {
                filesUrl.add(serviceAddress + name);
            }
        }
        for (String name : localFilesName) {
            if (allFilesNameOnServer.indexOf(name) < 0) {
                filesUrl.add(localFolder + name);
            }
        }

        return filesUrl;
    }

    public static ArrayList<String> getImagesPath(String folderPath) {
        if (folderPath == null || folderPath.equals("")) {
            return null;
        }
        File file = new File(folderPath);
        if (!file.exists()) {
            return null;
        }
        String[] files = file.list();
        ArrayList<String> pathList = null;
        if (files != null && files.length > 0) {
            pathList = new ArrayList<>();
            for (String fileName : files) {
                if (fileName.endsWith("jpg") || fileName.endsWith("bmp") || fileName.endsWith("png")) {
                    pathList.add(folderPath + "/" + fileName);
                }
            }
        }
        return pathList;
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                file.delete();
            } else {
                for (File file1 : files) {
                    deleteFile(file1);
                }
                deleteFile(file);
            }
        } else if (!file.isDirectory()) {
            file.delete();
        }

    }

}
