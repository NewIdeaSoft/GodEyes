package com.nisoft.inspectortools.bean.problem;

import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class Recode {
    private String mRecodeId;
    private String mType;
    private String mAuthor;
    private Date mDate;
    private String mDescription;
    private long mUpdateTime;

    public Recode() {
    }

    public Recode(String recodeId, String type, String author, Date date, String description, long updateTime) {
        super();
        mRecodeId = recodeId;
        mType = type;
        mAuthor = author;
        mDate = date;
        mDescription = description;
        mUpdateTime = updateTime;
    }

    public Recode(String recodeId) {
        mRecodeId = recodeId;
    }

    public String getRecodeId() {
        return mRecodeId;
    }

    public void setRecodeId(String recodeId) {
        mRecodeId = recodeId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }
}
