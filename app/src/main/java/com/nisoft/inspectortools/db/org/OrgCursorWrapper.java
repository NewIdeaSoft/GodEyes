package com.nisoft.inspectortools.db.org;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;
import com.nisoft.inspectortools.db.org.OrgDbSchema.OrgTable;
import com.nisoft.inspectortools.utils.StringFormatUtil;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class OrgCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public OrgCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Employee getEmployee(){
        Employee employee = new Employee();
        employee.setPhone(getString(getColumnIndex(EmployeeTable.Cols.PHONE)));
        employee.setName(StringFormatUtil.correctString(getString(getColumnIndex(EmployeeTable.Cols.NAME))));
        employee.setWorkNum(StringFormatUtil.correctString(getString(getColumnIndex(EmployeeTable.Cols.WORK_NUM))));
        employee.setOrgId(StringFormatUtil.correctString(getString(getColumnIndex(EmployeeTable.Cols.ORG_CODE))));
        if(getString(getColumnIndex(EmployeeTable.Cols.STATION_CODE))!=null) {
            employee.setPositionsId(StringFormatUtil.getStrings(getString(getColumnIndex(EmployeeTable.Cols.STATION_CODE))));
        }
        employee.setCompanyId(getString(getColumnIndex(EmployeeTable.Cols.COMPANY_ID)));
        return employee;
    }

    public OrgInfo getOrgInfo(){
        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgId(getString(getColumnIndex(OrgTable.Cols.ORG_CODE)));
        orgInfo.setOrgName(getString(getColumnIndex(OrgTable.Cols.ORG_NAME)));
        orgInfo.setOrgLevel(getInt(getColumnIndex(OrgTable.Cols.ORG_LEVEL)));
        orgInfo.setParentOrgId(getString(getColumnIndex(OrgTable.Cols.PARENT_CODE)));
        orgInfo.setCompanyId(getString(getColumnIndex(OrgTable.Cols.COMPANY_ID)));
        return orgInfo;
    }
}
