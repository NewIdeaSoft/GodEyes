package com.nisoft.inspectortools.bean.org;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nisoft.inspectortools.db.org.OrgCursorWrapper;
import com.nisoft.inspectortools.db.org.OrgDbHelper;
import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;
import com.nisoft.inspectortools.db.org.OrgDbSchema.OrgTable;
import com.nisoft.inspectortools.db.org.OrgDbSchema.PositionTable;

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
    public ArrayList<Employee> getEmployeesByOrg(String orgId) {
        OrgCursorWrapper cursor = query(EmployeeTable.NAME, EmployeeTable.Cols.ORG_CODE+"=?", new String[]{orgId}, EmployeeTable.Cols.ORG_CODE);
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
        if (employee.getStationsId() != null) {
            values.put(EmployeeTable.Cols.STATION_CODE, employee.getStationsId().toString());
        }
        values.put(EmployeeTable.Cols.COMPANY_ID, employee.getCompanyId());
        values.put(EmployeeTable.Cols.POSITION_ID,employee.getPositionId());
        return values;
    }

    public ContentValues getContentValues(OrgInfo orgInfo) {
        ContentValues values = new ContentValues();
        values.put(OrgTable.Cols.ORG_CODE, orgInfo.getOrgId());
        values.put(OrgTable.Cols.ORG_NAME, orgInfo.getOrgName());
        values.put(OrgTable.Cols.ORG_LEVEL, orgInfo.getOrgLevel());
        values.put(OrgTable.Cols.PARENT_CODE, orgInfo.getParentOrgId());
        values.put(OrgTable.Cols.COMPANY_ID, orgInfo.getCompanyId());
        return values;
    }
    public ContentValues getContentValues(PositionInfo positionInfo) {
        ContentValues values = new ContentValues();
        values.put(PositionTable.Cols.POSITION_ID, positionInfo.getPositionId());
        values.put(PositionTable.Cols.POSITION_NAME, positionInfo.getPositionName());
        values.put(PositionTable.Cols.MANAGE_LEVEL, positionInfo.getManageLevel());
        values.put(PositionTable.Cols.COMPANY_ID, positionInfo.getCompanyId());
        return values;
    }
    public void updatePositionInfo(PositionInfo positionInfo) {
        ContentValues values = getContentValues(positionInfo);
        if (values.size() > 0) {
            OrgCursorWrapper cursor = query(PositionTable.NAME,
                    PositionTable.Cols.POSITION_ID + "=?", new String[]{positionInfo.getPositionId()}, null);
            if (cursor.getCount() > 0) {
                int i = mDataBase.update(PositionTable.NAME, values,
                        PositionTable.Cols.POSITION_ID + "=?", new String[]{positionInfo.getPositionId()});
            } else {
                long j = mDataBase.insert(PositionTable.NAME, null, values);
            }
            cursor.close();
        }
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

    public void updateOrgInfo(OrgInfo org) {
        ContentValues values = getContentValues(org);
        if (values.size() > 0) {
            OrgCursorWrapper cursor = query(OrgTable.NAME,
                    OrgTable.Cols.ORG_CODE + "=?", new String[]{org.getOrgId()}, null);
            if (cursor.getCount() > 0) {
                int i = mDataBase.update(OrgTable.NAME, values,
                        OrgTable.Cols.ORG_CODE + "=?", new String[]{org.getOrgId()});
            } else {
                long j = mDataBase.insert(OrgTable.NAME, null, values);
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

    public void updateOrgs(ArrayList<OrgInfo> orgInfos) {
        if (orgInfos == null || orgInfos.size() == 0) {
            return;
        }
        for (OrgInfo org : orgInfos) {
            updateOrgInfo(org);
        }
    }
    public void updatePositions(ArrayList<PositionInfo> positionInfos) {
        if (positionInfos == null || positionInfos.size() == 0) {
            return;
        }
        for (PositionInfo p : positionInfos) {
            updatePositionInfo(p);
        }
    }
    public PositionInfo findPositionById(String positionId){
        OrgCursorWrapper cursor = query(PositionTable.NAME, PositionTable.Cols.POSITION_ID + "=?", new String[]{positionId}, null);
        PositionInfo positionInfo = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            positionInfo = cursor.getResultPosition();
        }
        cursor.close();
        return positionInfo;
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

    public OrgInfo findOrgInfoById(String org_code) {
        OrgCursorWrapper cursor = query(OrgTable.NAME, OrgTable.Cols.ORG_CODE + "=?", new String[]{org_code}, null);
        OrgInfo org = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            org = cursor.getOrgInfo();
        }
        cursor.close();
        return org;
    }

    public ArrayList<OrgInfo> findOrgsByParent(String parentId) {
        OrgCursorWrapper cursor = query(OrgTable.NAME, OrgTable.Cols.PARENT_CODE + "=?", new String[]{parentId}, null);
        ArrayList<OrgInfo> orgs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                OrgInfo org = cursor.getOrgInfo();
                orgs.add(org);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return orgs;
    }
}
