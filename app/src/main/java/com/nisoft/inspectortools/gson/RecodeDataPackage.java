package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;

/**
 * Created by Administrator on 2017/6/29.
 */

public class RecodeDataPackage {
    private String mName;
    private MaterialInspectRecode mRecode;
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public MaterialInspectRecode getRecode() {
        return mRecode;
    }
    public void setRecode(MaterialInspectRecode recode) {
        mRecode = recode;
    }
}
