package com.nisoft.inspectortools.bean.org;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nisoft.inspectortools.db.org.OrgCursorWrapper;
import com.nisoft.inspectortools.db.org.OrgDbHelper;
import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class OrgLab {
    private static OrgLab sOrgLab;
    private Context mContext;
    private SQLiteDatabase mDataBase;

    private OrgLab(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new OrgDbHelper(mContext, 1).getWritableDatabase();
    }

    public static OrgLab getOrgLab(Context context) {
        if (sOrgLab == null) {
            sOrgLab = new OrgLab(context);
        }
        return sOrgLab;
    }

    public OrgCursorWrapper query(String table, String selection, String[] selectionArgs, String orderBy) {
        Cursor cursor = mDataBase.query(table, null, selection, selectionArgs, null, null, orderBy);
        return new OrgCursorWrapper(cursor);
    }

    public ArrayList<Employee> getEmployees() {
        OrgCursorWrapper cursor = query(EmployeeTable.NAME, null, null, EmployeeTable.Cols.ORG_CODE);
        ArrayList<Employee> employees = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                employees.add(cursor.getEmployee());
                cursor.moveToNext();
            }
        }
        cursor.close();
        return employees;
    }

    public ContentValues getContentValues(Employee employee) {
        ContentValues values = new ContentValues();
        values.put(EmployeeTable.Cols.PHONE, employee.getPhone());
        values.put(EmployeeTable.Cols.NAME, employee.getName());
        values.put(EmployeeTable.Cols.WORK_NUM, employee.getWorkNum());
        values.put(EmployeeTable.Cols.ORG_CODE, employee.getOrgId());
        if (employee.getPositionsId() != null) {
            values.put(EmployeeTable.Cols.STATION_CODE, employee.getPositionsId().toString());
        }

        return values;
    }

    public void updateEmployee(Employee employee) {
        ContentValues values = getContentValues(employee);
        if (values.size() > 0) {
            OrgCursorWrapper cursor = query(EmployeeTable.NAME,
                    EmployeeTable.Cols.PHONE + "=?", new String[]{employee.getPhone()}, null);
            if (cursor.getCount() > 0) {
                int i = mDataBase.update(EmployeeTable.NAME, values,
                        EmployeeTable.Cols.PHONE + "=?", new String[]{employee.getPhone()});
            } else {
                long j = mDataBase.insert(EmployeeTable.NAME, null, values);
            }
            cursor.close();
        }
    }

    public void updateEmployee(ArrayList<Employee> employees) {
        if (employees == null || employees.size() == 0) {
            return;
        }
        for (Employee employee : employees) {
            updateEmployee(employee);
        }
    }

    public Employee findEmployeeById(String phone) {
        OrgCursorWrapper cursor = query(EmployeeTable.NAME, EmployeeTable.Cols.PHONE + "=?", new String[]{phone}, null);
        Employee employee = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            employee = cursor.getEmployee();
        }
        cursor.close();
        return employee;

    }

}
