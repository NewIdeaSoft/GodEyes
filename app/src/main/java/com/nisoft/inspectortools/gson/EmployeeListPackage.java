package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.org.Employee;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class EmployeeListPackage {
    private ArrayList<Employee> mEmployees;

    public ArrayList<Employee> getEmployees() {
        return mEmployees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        mEmployees = employees;
    }
}
