package com.nisoft.inspectortools.bean.problem1;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ProblemAnalysis {
    private String mProblemId;
    private String mAnalysisId;
    private String mAnalystId;
    private Date mAnalysisDate;
    private String mPicFolderPath;
    private String mDescriptionText;
    private long mUpdateTime;

    public String getProblemId() {
        return mProblemId;
    }

    public void setProblemId(String problemId) {
        mProblemId = problemId;
    }

    public String getAnalysisId() {
        return mAnalysisId;
    }

    public void setAnalysisId(String analysisId) {
        mAnalysisId = analysisId;
    }

    public String getAnalystId() {
        return mAnalystId;
    }

    public void setAnalystId(String analystId) {
        mAnalystId = analystId;
    }

    public Date getAnalysisDate() {
        return mAnalysisDate;
    }

    public void setAnalysisDate(Date analysisDate) {
        mAnalysisDate = analysisDate;
    }

    public String getPicFolderPath() {
        return mPicFolderPath;
    }

    public void setPicFolderPath(String picFolderPath) {
        mPicFolderPath = picFolderPath;
    }

    public String getDescriptionText() {
        return mDescriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        mDescriptionText = descriptionText;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }
}
