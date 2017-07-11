package com.nisoft.inspectortools.bean.problem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ProblemRecode extends ImageRecode {
    private String mAddress;
    private ArrayList<String> mSuspects;
    private String mTitle;

    public ProblemRecode() {
    }

    public ProblemRecode(String recodeId) {
        super(recodeId);
    }

    public ProblemRecode(String recodeId, String type, String author, Date date, String description, long updateTime, ArrayList<String> imagesName, String adress, ArrayList<String> suspects, String title) {
        super(recodeId, type, author, date, description, updateTime, imagesName);
        mAddress = adress;
        mSuspects = suspects;
        mTitle = title;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public ArrayList<String> getSuspects() {
        return mSuspects;
    }

    public void setSuspects(ArrayList<String> suspects) {
        mSuspects = suspects;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
