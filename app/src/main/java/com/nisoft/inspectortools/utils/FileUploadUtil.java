package com.nisoft.inspectortools.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

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
    public static final String UPLOAD_ADRESS="http://192.168.31.189:8080/InspectorTools/FileUploadServeice";
    public static void uploadFile(File file, final Context context, final ProgressDialog dialog){
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        final String fileName = file.getName();
        String [] strings = file.getName().split(".");
        String fileType = strings[strings.length-1];
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileType,fileName,fileBody)
                .build();
        dialog.setMessage(fileName+"正在上传...");
        dialog.show();
        HttpUtil.sendPostRequest(UPLOAD_ADRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, fileName+"上传失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
            }
        });
    }

    public static void uploadFileDir(File dir,Context context,ProgressDialog dialog){
        File [] files = dir.listFiles();
        if (files.length>0){
            for (File file:files){
                FileUploadUtil.uploadFile(file,context,dialog);
            }
        }
    }

}
