package com.nisoft.inspectortools.bean.org;

import android.content.Context;

/**
 * Created by Administrator on 2017/6/28.
 */

public class UserLab {
    private static UserLab sUserLab;
    private Employee mEmployee;
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
}
