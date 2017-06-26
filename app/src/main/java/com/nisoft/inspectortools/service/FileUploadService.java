package com.nisoft.inspectortools.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nisoft.inspectortools.utils.FileUploadUtil;

import java.io.File;

public class FileUploadService extends Service {
    private String uploadUrl;
    public FileUploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploadUrl = intent.getStringExtra("folder_path");
        Log.e("uploadUrl", uploadUrl);
        File folder = new File(uploadUrl);
        FileUploadUtil.uploadFile(folder);
        return super.onStartCommand(intent, flags, startId);
    }

}
