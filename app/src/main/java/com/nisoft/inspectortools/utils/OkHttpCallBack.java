package com.nisoft.inspectortools.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/5.
 */

public abstract class OkHttpCallBack implements Callback {
    private String mFailureInfo;
    private ProgressDialog mDialog;
    private Context mContext;

    public OkHttpCallBack(String failureInfo, Context context) {
        mFailureInfo = failureInfo;
        mContext = context;
    }

    public OkHttpCallBack(String failureInfo, ProgressDialog dialog, Context context) {
        mFailureInfo = failureInfo;
        mDialog = dialog;
        mContext = context;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                Toast.makeText(mContext, mFailureInfo, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        handleResponseBackThread(result);
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handleResponseUIThread(result);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
            }
        });

    }

    protected abstract void handleResponseUIThread(String result);
    protected abstract void handleResponseBackThread(String result);
}
