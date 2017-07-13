package com.nisoft.inspectortools.bean.org;

/**
 * Created by NewIdeaSoft on 2017/7/12.
 */

public class PositionInfo {
    private String mPositionId;
    private String mPositionName;
    private int mManageLevel;
    private String mCompanyId;

    public String getPositionId() {
        return mPositionId;
    }

    public void setPositionId(String positionId) {
        mPositionId = positionId;
    }

    public String getPositionName() {
        return mPositionName;
    }

    public void setPositionName(String positionName) {
        mPositionName = positionName;
    }

    public int getManageLevel() {
        return mManageLevel;
    }

    public void setManageLevel(int manageLevel) {
        mManageLevel = manageLevel;
    }

    public String getCompanyId() {
        return mCompanyId;
    }

    public void setCompanyId(String companyId) {
        mCompanyId = companyId;
    }
}
