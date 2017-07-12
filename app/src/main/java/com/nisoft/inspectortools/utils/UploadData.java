package com.nisoft.inspectortools.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/12.
 */

public class UploadData {
    private Context mContext;
    private String mUrl;
    private RequestBody mRequestBody;
    private UploadStateListener mListener;

    public interface UploadStateListener {
        void onStart();

        void onFailed();

        void onFinish(String result);
    }

    public UploadData(Context context, String url, RequestBody requestBody, UploadStateListener listener) {
        mContext = context;
        mUrl = url;
        mRequestBody = requestBody;
        mListener = listener;
    }

    public void upload() {
        mListener.onStart();
        HttpUtil.sendPostRequest(mUrl, mRequestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onFailed();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onFinish(result);
                    }
                });
            }
        });
    }
}
