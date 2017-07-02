package com.nisoft.inspectortools.bean.problem;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ImageRecode extends Recode {
    private String mImagesFolderPath;

    public ImageRecode() {
    }

    public ImageRecode(String recodeId) {
        super(recodeId);
    }

    public String getImagesFolderPath() {
        return mImagesFolderPath;
    }

    public void setImagesFolderPath(String imagesFolderPath) {
        mImagesFolderPath = imagesFolderPath;
    }
}
