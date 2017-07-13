package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.PositionInfo;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class EmployeeListPackage {
    private ArrayList<Employee> mEmployees;
    private ArrayList<OrgInfo> mOrgList;
    private ArrayList<PositionInfo> mPositionList;
    public ArrayList<Employee> getEmployees() {
        return mEmployees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        mEmployees = employees;
    }

    public ArrayList<OrgInfo> getOrgList() {
        return mOrgList;
    }

    public void setOrgList(ArrayList<OrgInfo> orgList) {
        mOrgList = orgList;
    }

    public ArrayList<PositionInfo> getPositionList() {
        return mPositionList;
    }

    public void setPositionList(ArrayList<PositionInfo> positionList) {
        mPositionList = positionList;
    }
}
