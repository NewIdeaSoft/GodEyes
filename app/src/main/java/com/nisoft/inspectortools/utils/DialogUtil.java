package com.nisoft.inspectortools.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/6/14.
 */

public class DialogUtil {
    public static void showProgressDialog(Context context, ProgressDialog dialog, String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
        }
        dialog.setMessage(message);
        dialog.show();
    }
}
