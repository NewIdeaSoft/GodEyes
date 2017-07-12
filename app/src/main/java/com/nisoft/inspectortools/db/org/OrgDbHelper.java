package com.nisoft.inspectortools.db.org;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;
import com.nisoft.inspectortools.db.org.OrgDbSchema.OrgTable;
import com.nisoft.inspectortools.db.org.OrgDbSchema.PositionTable;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class OrgDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "org.db";
    private static final String CREATE_EMPLOYEE = "create table " +
            EmployeeTable.NAME + "(_id integer primary key autoincrement," +
            EmployeeTable.Cols.PHONE + " unique," +
            EmployeeTable.Cols.NAME + "," +
            EmployeeTable.Cols.WORK_NUM + "," +
            EmployeeTable.Cols.ORG_CODE + "," +
            EmployeeTable.Cols.COMPANY_ID + "," +
            EmployeeTable.Cols.POSITION_ID + "," +
            EmployeeTable.Cols.STATION_CODE + ")";
    private static final String CREATE_ORG = "create table " +
            OrgTable.NAME + "(_id integer primary key autoincrement," +
            OrgTable.Cols.ORG_CODE + " unique," +
            OrgTable.Cols.ORG_NAME + "," +
            OrgTable.Cols.PARENT_CODE + "," +
            OrgTable.Cols.COMPANY_ID + "," +
            OrgTable.Cols.ORG_LEVEL + ")";

    private static final String CREATE_POSITION= "create table " +
            PositionTable.NAME + "(_id integer primary key autoincrement," +
            PositionTable.Cols.POSITION_ID + " unique," +
            PositionTable.Cols.POSITION_NAME + "," +
            PositionTable.Cols.COMPANY_ID + "," +
            PositionTable.Cols.MANAGE_LEVEL + ")";

    public OrgDbHelper(Context context, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEE);
        db.execSQL(CREATE_ORG);
        db.execSQL(CREATE_POSITION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
