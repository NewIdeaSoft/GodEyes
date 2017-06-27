package com.nisoft.inspectortools.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ImageLoadUtils {
    private Context mContext;
    public ImageLoadUtils(Context context){
        mContext = context;
    }
    public long[] downLoadImage(String dirPath,String imagesAddress,ArrayList<String> filesOnServer){
        File dir = new File(dirPath);
        ArrayList<String> localImageFilesName = new ArrayList<>();
        String[] picsName = dir.list();
        for (int i =0;i<picsName.length;i++){
            String [] strings = picsName[i].split("\\.");
            String type = strings[strings.length-1];
            if (type.equals("jpg")||type.equals("bmp")){
                localImageFilesName.add(picsName[i]);
            }
        }
        ArrayList<String> downloadFilesName = getUnExitItem(localImageFilesName,filesOnServer);
        long[] downloadId;
        if (downloadFilesName.size()>0){
            downloadId = new long[downloadFilesName.size()];
            for (int j =0;j<downloadFilesName.size();j++){
                String imageName = downloadFilesName.get(j);
                String address = imagesAddress + imageName;
                String path = dirPath+imageName;
                downloadId[j]=downloadImage(address,path,"正在下载第"+j+"张图片");
            }
            return downloadId;
        }
        return null;
    }
    private long downloadImage(String address,String localPath,String title){
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(address));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(title);
        File file = new File(localPath);
        request.setDestinationUri(Uri.fromFile(file));
        long downloadId = downloadManager.enqueue(request);
        return downloadId;
    }

    private ArrayList<String> getUnExitItem(ArrayList<String> list1, ArrayList<String> list2){
        for (int i =0;i<list1.size();i++){
            if (list2.indexOf(list1.get(i))>0){
                list2.remove(list1.get(i));
            }
        }
        return list2;
    }
}
