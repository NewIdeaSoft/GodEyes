package com.nisoft.inspectortools.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nisoft.inspectortools.utils.FileUploadUtil;

import java.io.File;

public class FileUploadService extends Service {
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
        String uploadUrl = intent.getStringExtra("folder_path");
        String companyId = intent.getStringExtra("company_id");
        String recodeType = intent.getStringExtra("recode_type");
        Log.e("uploadUrl", uploadUrl);
        File folder = new File(uploadUrl);
        FileUploadUtil.uploadFile(folder,companyId,recodeType);
        return super.onStartCommand(intent, flags, startId);
    }

}
