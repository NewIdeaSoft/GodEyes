package com.nisoft.inspectortools.utils;

import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/30.
 */

public class VolumeImageDownLoad {
    private static int count = 0;
    private ArrayList<String> mImageUrlList;
    private String mDownloadPath;
    private DownloadStateListener mListener;

    public VolumeImageDownLoad(ArrayList<String> imageUrlList, String downloadPath, DownloadStateListener listener) {
        mImageUrlList = imageUrlList;
        mDownloadPath = downloadPath;
        mListener = listener;
    }

    public interface DownloadStateListener {
        void onStart();
        void onFailed();

        void onFinish();
    }

    public void startDownload() {
        mListener.onStart();
        for (String url : mImageUrlList) {
            downloadImage(url);
        }
    }

    private void downloadImage(final String url) {
        HttpUtil.sendGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mListener.onFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                File folder = new File(mDownloadPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(folder, getImageName(url));
                if (!file.exists()) {
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    try {
                        is = response.body().byteStream();
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        mListener.onFailed();
                    } finally {
                        fos.close();
                        is.close();
                        count++;
                        if (count == mImageUrlList.size()) {
                            mListener.onFinish();
                        }
                    }
                }else{
                    count++;
                }
            }
        });
    }

    /***
     *
     * @param url 文件的路径或网址
     * @return 文件名
     */
    private String getImageName(String url) {
        String[] strings = url.split("/");
        return strings[strings.length - 1];
    }
}
