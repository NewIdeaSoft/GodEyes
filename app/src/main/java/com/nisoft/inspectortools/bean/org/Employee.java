package com.nisoft.inspectortools.bean.org;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/6/10.
 */

public class Employee {
    private String mName;
    private String mPhone;
    private String mWorkNum;
    private String mOrgId;
    private String mCompanyId;
    private ArrayList<String> mStationsId;
    private String mPositionId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String number) {
        mPhone = number;
    }

    public String getWorkNum() {
        return mWorkNum;
    }

    public void setWorkNum(String workNum) {
        mWorkNum = workNum;
    }

    public String getOrgId() {
        return mOrgId;
    }

    public void setOrgId(String orgId) {
        mOrgId = orgId;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }

    public ArrayList<String> getStationsId() {
        return mStationsId;
    }

    public void setStationsId(ArrayList<String> stationsId) {
        mStationsId = stationsId;
    }

    public String getPositionId() {
        return mPositionId;
    }

    public void setPositionId(String positionId) {
        mPositionId = positionId;
    }
}
