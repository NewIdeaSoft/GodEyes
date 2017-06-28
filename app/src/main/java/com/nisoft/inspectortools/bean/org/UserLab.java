package com.nisoft.inspectortools.bean.org;

import android.content.Context;

/**
 * Created by Administrator on 2017/6/28.
 */

public class UserLab {
    private static UserLab sUserLab;
    private Employee mEmployee;
    private User mUser;
    private Company mCompany;
    private Context mContext;

    private UserLab(Context context){
        mContext = context.getApplicationContext();
    }

    public static UserLab getUserLab(Context context){
        if (sUserLab==null){
            sUserLab = new UserLab(context);
        }
        return sUserLab;
    }

    public Employee getEmployee(){
        return mEmployee;
    }

    public void setEmployee(Employee employee) {
        mEmployee = employee;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Company getCompany() {
        return mCompany;
    }

    public void setCompany(Company company) {
        mCompany = company;
    }
}
