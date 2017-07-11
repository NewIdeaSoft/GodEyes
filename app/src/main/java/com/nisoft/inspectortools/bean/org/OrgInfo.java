package com.nisoft.inspectortools.bean.org;

/**
 * Created by NewIdeaSoft on 2017/6/10.
 */

public class OrgInfo {
    private String mOrgId;
    private String mOrgName;
    private int mOrgLevel;
    private String mParentOrgId;
    private String mCompanyId;



    public String getOrgName() {
        return mOrgName;
    }

    public void setOrgName(String orgName) {
        mOrgName = orgName;
    }

    public int getOrgLevel() {
        return mOrgLevel;
    }

    public void setOrgLevel(int orgLevel) {
        mOrgLevel = orgLevel;
    }

    public String getOrgId() {
        return mOrgId;
    }

    public void setOrgId(String orgId) {
        mOrgId = orgId;
    }

    public String getParentOrgId() {
        return mParentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        mParentOrgId = parentOrgId;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }
}
