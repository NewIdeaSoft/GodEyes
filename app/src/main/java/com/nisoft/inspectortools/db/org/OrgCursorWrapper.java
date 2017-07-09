package com.nisoft.inspectortools.db.org;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;
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
        employee.setName(getString(getColumnIndex(EmployeeTable.Cols.NAME)));
        employee.setWorkNum(getString(getColumnIndex(EmployeeTable.Cols.WORK_NUM)));
        employee.setOrgId(getString(getColumnIndex(EmployeeTable.Cols.ORG_CODE)));
        if(getString(getColumnIndex(EmployeeTable.Cols.PHONE))!=null) {
            employee.setPositionsId(StringFormatUtil.getStrings(getString(getColumnIndex(EmployeeTable.Cols.PHONE))));
        }
        employee.setPhone(getString(getColumnIndex(EmployeeTable.Cols.PHONE)));
        return employee;
    }
}
