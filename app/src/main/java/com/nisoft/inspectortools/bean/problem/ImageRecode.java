package com.nisoft.inspectortools.bean.problem;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ImageRecode extends Recode {
    private ArrayList<String> mImagesNameOnserver;

    public ImageRecode() {
    }

    

    public ImageRecode(String recodeId) {
        super(recodeId);
    }

    public ArrayList<String> getImagesNameOnserver() {
        return mImagesNameOnserver;
    }

    public void setImagesNameOnserver(ArrayList<String> imagesNameOnserver) {
        mImagesNameOnserver = imagesNameOnserver;
    }
}
