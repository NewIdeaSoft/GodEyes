package com.nisoft.inspectortools.bean.org;

import com.nisoft.inspectortools.bean.product.Product;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/18.
 */

public class EmployeeDataPackage {
    private Employee mEmployee;
    private ArrayList<String> mOrgInfo;
    private ArrayList<String> mStations;
    private ArrayList<OrgInfo> mOrgs;
    private int mOrgLevels;

    public Employee getEmployee() {
        return mEmployee;
    }

    public void setEmployee(Employee employee) {
        mEmployee = employee;
    }

    public ArrayList<String> getOrgInfo() {
        return mOrgInfo;
    }

    public void setOrgInfo(ArrayList<String> orgInfo) {
        mOrgInfo = orgInfo;
    }

    public ArrayList<String> getStations() {
        return mStations;
    }

    public void setStations(ArrayList<String> stations) {
        mStations = stations;
    }

    public ArrayList<OrgInfo> getOrgs() {
        return mOrgs;
    }

    public void setOrgs(ArrayList<OrgInfo> orgs) {
        mOrgs = orgs;
    }

    public int getOrgLevels() {
        return mOrgLevels;
    }

    public void setOrgLevels(int orgLevels) {
        mOrgLevels = orgLevels;
    }
}
