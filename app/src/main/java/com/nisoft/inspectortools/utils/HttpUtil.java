package com.nisoft.inspectortools.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/11.
 */

public class HttpUtil {
    public static final String ADDRESS_LOGIN = "http://192.168.31.189:8080/InspectorTools/LoginServlet";
    public static final String ADDRESS_UPLOAD = "http://192.168.31.189:8080/InspectorTools/FileUploadServlet";
    public static final String ADDRESS_USERINFO = "http://192.168.31.189:8080/InspectorTools/UserInfoServlet";
    public static final String ADDRESS_JOBINFO = "http://192.168.31.189:8080/InspectorTools/JobInfoServlet";
    public static final String ADDRESS_DOWNLOAD_LAN_CHI = "http://192.168.31.189:8080/InspectorTools/chi-sim";
    public static void sendOkHttpRequest(String address, RequestBody body, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
