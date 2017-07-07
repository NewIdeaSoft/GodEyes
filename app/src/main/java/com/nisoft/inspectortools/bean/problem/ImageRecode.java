package com.nisoft.inspectortools.bean.problem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ImageRecode extends Recode {
    private ArrayList<String> mImagesNameOnServer;

    public ImageRecode() {
    }

    public ImageRecode(String recodeId, String type, String author, Date date, String description, long updateTime, ArrayList<String> imagesName) {
        super(recodeId, type, author, date, description, updateTime);
        mImagesNameOnServer = imagesName;
    }

    public ImageRecode(String recodeId) {
        super(recodeId);
    }

    public ArrayList<String> getImagesNameOnServer() {
        return mImagesNameOnServer;
    }

    public void setImagesNameOnserver(ArrayList<String> imagesNameOnServer) {
        mImagesNameOnServer = imagesNameOnServer;
    }


}
