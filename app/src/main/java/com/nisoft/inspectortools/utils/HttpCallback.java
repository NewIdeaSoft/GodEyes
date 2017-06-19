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
 * Created by Administrator on 2017/6/19.
 */

public abstract class HttpCallback implements Callback {
    private String mFailureToast;
    private Activity mContext;
    private ProgressDialog mDialog;
    private Response mResponse;
    public HttpCallback(Activity context,String failureToast,ProgressDialog dialog){
        mFailureToast = failureToast;
        mContext =context;
        mDialog = dialog;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                Toast.makeText(mContext,mFailureToast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        mResponse = response;
        handResponse();
    }

    protected abstract void handResponse() throws IOException;

    public String getResult () throws IOException {
        return mResponse.body().string();
    }
}
