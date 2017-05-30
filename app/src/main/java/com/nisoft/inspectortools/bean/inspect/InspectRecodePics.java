package com.nisoft.inspectortools.bean.inspect;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/22.
 */

public class InspectRecodePics {
    private String mJobNum;
    private ArrayList<String> mPicPath;
    private Date mDate;
    private String mType;

    public String getJobNum() {
        return mJobNum;
    }

    public void setJobNum(String jobNum) {
        mJobNum = jobNum;
    }

    public ArrayList<String> getPicPath() {
        return mPicPath;
    }

    public void setPicPath(ArrayList<String> picPath) {
        mPicPath = picPath;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
