package com.nisoft.inspectortools.bean.problem1;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/30.
 */

public class Program {
    private String mProblemId;
    private String mProgramId;
    private String mAuthorId;
    private Date mDraftDate;
    private String mPicFolderPath;
    private String mDescriptionText;
    private long mUpdateTime;
    private String executorId;
    private ArrayList<ExecutionRecode> mExecutionRecodes;

    public String getProblemId() {
        return mProblemId;
    }

    public void setProblemId(String problemId) {
        mProblemId = problemId;
    }

    public String getProgramId() {
        return mProgramId;
    }

    public void setProgramId(String programId) {
        mProgramId = programId;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }

    public Date getDraftDate() {
        return mDraftDate;
    }

    public void setDraftDate(Date draftDate) {
        mDraftDate = draftDate;
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

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public ArrayList<ExecutionRecode> getExecutionRecodes() {
        return mExecutionRecodes;
    }

    public void setExecutionRecodes(ArrayList<ExecutionRecode> executionRecodes) {
        mExecutionRecodes = executionRecodes;
    }
}
