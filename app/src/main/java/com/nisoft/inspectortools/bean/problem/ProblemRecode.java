package com.nisoft.inspectortools.bean.problem;

import java.util.ArrayList;

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
