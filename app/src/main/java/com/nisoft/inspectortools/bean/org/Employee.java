package com.nisoft.inspectortools.bean.org;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/6/10.
 */

public class Employee {
    private String mName;
    private String mWorkNum;
    private String mPhone;
    private String mOrgCode;
    private ArrayList<String> mStationsCode;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getWorkNum() {
        return mWorkNum;
    }

    public void setWorkNum(String workNum) {
        mWorkNum = workNum;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getOrgCode() {
        return mOrgCode;
    }

    public void setOrgCode(String orgCode) {
        mOrgCode = orgCode;
    }

    public ArrayList<String> getStationsCode() {
        return mStationsCode;
    }

    public void setStationsCode(ArrayList<String> stationsCode) {
        mStationsCode = stationsCode;
    }
}
