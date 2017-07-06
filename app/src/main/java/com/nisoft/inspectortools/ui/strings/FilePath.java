package com.nisoft.inspectortools.ui.strings;

import android.os.Environment;

/**
 * Created by Administrator on 2017/7/6.
 */

public class FilePath {
    public static final String APP_DATA_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/工作相册/";
    public static final String METAIL_DATA_PATH = APP_DATA_PATH+"原材料检验/";
    public static final String PROBLEM_DATA_PATH = APP_DATA_PATH+"质量问题/";
}
