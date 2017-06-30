package com.nisoft.inspectortools.bean.problem1;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExecutionRecode {
    private String mDescription;
    private Date mDate;

    public ExecutionRecode(String description, Date date) {
        mDescription = description;
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
