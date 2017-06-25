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
    public static void uploadFile(File folder){
        File [] files = folder.listFiles();
        if(files.length>0) {
            String org_id = "JXCZ";
            String folder_name = folder.getName();
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
//                    .addFormDataPart("org_id",org_id)
//                    .addFormDataPart("recode_type","material_inspect/金属材料")
//                    .addFormDataPart("folder_name",folder_name);
            for (File file : files){
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
                String fileName = file.getName();
                Log.e("fileName",fileName);
                String [] strings = fileName.split(".");

                Log.e("fileName",strings.length+"");
//                String fileType = strings[strings.length-1];
                String fileType = "jpg";
                builder.addFormDataPart(fileType,fileName,fileBody);
            }
            RequestBody body = builder.build();
            HttpUtil.sendPostRequest(HttpUtil.SERVLET_UPLOAD, body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("upload:","failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e("upload:","completed!");
                }
            });
        }else{
            return;
        }
    }

    public static void uploadFileDir(File dir){
        File [] files = dir.listFiles();
        if (files.length>0){
            for (File file:files){
                FileUploadUtil.uploadFile(file);
            }
        }
    }

}
