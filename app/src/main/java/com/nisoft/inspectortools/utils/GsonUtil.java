package com.nisoft.inspectortools.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Administrator on 2017/7/11.
 */

public class GsonUtil {
    public static Gson getDateFormatGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    }
}
