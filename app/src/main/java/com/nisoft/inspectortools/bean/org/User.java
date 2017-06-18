package com.nisoft.inspectortools.bean.org;

/**
 * Created by NewIdeaSoft on 2017/6/10.
 */

public class User {
    private String mName;
    private String mPassWord;
    private String mOrgId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPassWord() {
        return mPassWord;
    }

    public void setPassWord(String passWord) {
        mPassWord = passWord;
    }

    public String getOrgId() {
        return mOrgId;
    }

    public void setOrgId(String orgId) {
        mOrgId = orgId;
    }
}
