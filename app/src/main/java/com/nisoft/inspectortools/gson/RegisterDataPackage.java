package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.org.Company;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/18.
 */

public class RegisterDataPackage {
    ArrayList<Company> mCompanies;

    public ArrayList<Company> getCompanies() {
        return mCompanies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        mCompanies = companies;
    }
}
