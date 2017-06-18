package com.nisoft.inspectortools.bean.org;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/6/10.
 */

public class Company {
    private String mOrgCode;
    private String mOrgName;
    private ArrayList<String> mOrgStructure;

    public String getOrgCode() {
        return mOrgCode;
    }

    public void setOrgCode(String orgCode) {
        mOrgCode = orgCode;
    }

    public String getOrgName() {
        return mOrgName;
    }

    public void setOrgName(String orgName) {
        mOrgName = orgName;
    }

    public ArrayList<String> getOrgStructure() {
        return mOrgStructure;
    }

    public void setOrgStructure(ArrayList<String> orgStructure) {
        mOrgStructure = orgStructure;
    }
}
