package com.nisoft.inspectortools.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
/**
 * Created by Administrator on 2017/6/28.
 */

public class GlideCache implements GlideModule {
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/工作记录/";
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(ROOT_PATH,1024*1024*1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
