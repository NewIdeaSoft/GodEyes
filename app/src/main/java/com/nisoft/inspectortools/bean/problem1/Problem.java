package com.nisoft.inspectortools.bean.problem1;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/30.
 */

public class Problem {
    private String mProblemId;
    private String mType;
    private String mTitle;
    private Date mDate;
    private String mAddress;
    private String mDiscover;
    private ArrayList<String> mSuspects;
    private String mDetailedText;
    private String mFolderPath;
    private long mUpdateTime;
    private String mAnalystId;
    private String mProgramId;

    public Problem() {
    }
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDiscover() {
        return mDiscover;
    }

    public void setDiscover(String discover) {
        mDiscover = discover;
    }

    public ArrayList<String> getSuspects() {
        return mSuspects;
    }

    public void setSuspects(ArrayList<String> suspects) {
        mSuspects = suspects;
    }

    public String getDetailedText() {
        return mDetailedText;
    }

    public void setDetailedText(String detailedText) {
        mDetailedText = detailedText;
    }

    public String getProblemId() {
        return mProblemId;
    }

    public void setProblemId(String problemId) {
        mProblemId = problemId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getFolderPath() {
        return mFolderPath;
    }

    public void setFolderPath(String folderPath) {
        mFolderPath = folderPath;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }

    public String getAnalystId() {
        return mAnalystId;
    }

    public void setAnalystId(String analystId) {
        mAnalystId = analystId;
    }

    public String getProgramId() {
        return mProgramId;
    }

    public void setProgramId(String programId) {
        mProgramId = programId;
    }
}
