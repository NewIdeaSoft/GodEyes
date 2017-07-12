package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.Position;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class EmployeeListPackage {
    private ArrayList<Employee> mEmployees;
    private ArrayList<Position> mPositions;
    private ArrayList<OrgInfo> mOrgInfoList;

    public ArrayList<Position> getPositions() {
        return mPositions;
    }

    public void setPositions(ArrayList<Position> positions) {
        mPositions = positions;
    }

    public ArrayList<OrgInfo> getOrgInfoList() {
        return mOrgInfoList;
    }

    public void setOrgInfoList(ArrayList<OrgInfo> orgInfoList) {
        mOrgInfoList = orgInfoList;
    }

    public ArrayList<Employee> getEmployees() {
        return mEmployees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        mEmployees = employees;
    }
}
