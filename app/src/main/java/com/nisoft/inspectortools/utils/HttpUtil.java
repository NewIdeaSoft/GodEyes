package com.nisoft.inspectortools.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/6/11.
 */

public class HttpUtil {
    public static final String SERVLET_LOGIN = "LoginServlet";
    public static final String SERVLET_UPLOAD = "FileUploadServlet";
    public static final String SERVLET_USERINFO = "UserInfoServlet";
    public static final String SERVLET_JOBINFO = "JobInfoServlet";
    public static final String SERVLET_DOWNLOAD_LAN_CHI = "chi-sim";
    public static final String ADRESS_MAIN = "http://47.93.191.62:8080/InspectorTools/";
    public static void sendOkHttpRequest(String address, RequestBody body, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).post(body).build();
        client.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
