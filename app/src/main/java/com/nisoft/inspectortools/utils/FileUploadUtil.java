package com.nisoft.inspectortools.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/12.
 */

public class FileUploadUtil {
    private OnUploadCompleted mOnUploadCompleted;

    public FileUploadUtil(OnUploadCompleted onUploadCompleted) {
        mOnUploadCompleted = onUploadCompleted;
    }

    public void uploadFile(File folder, String companyId, String recodeType, String folderName) {
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("org_id", companyId)
                    .addFormDataPart("recode_type", recodeType)
                    .addFormDataPart("folder_name", folderName);
            for (File file : files) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                String fileName = file.getName();
                String[] strings = fileName.split("\\.");
                String fileType = strings[strings.length - 1];
                Log.e("fileType:", fileType);
                builder.addFormDataPart(fileType, fileName, fileBody);
            }
            RequestBody body = builder.build();
            HttpUtil.sendPostRequest(HttpUtil.SERVLET_UPLOAD, body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("upload:", "failure");
                    mOnUploadCompleted.onFailed();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("upload:", "completed!");
                    mOnUploadCompleted.onFinish();
                }
            });
        } else {
            mOnUploadCompleted.onFinish();
        }
    }

    public interface OnUploadCompleted {
        void onFailed();

        void onFinish();
    }
}
