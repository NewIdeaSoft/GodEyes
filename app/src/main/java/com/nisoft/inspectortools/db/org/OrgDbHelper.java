package com.nisoft.inspectortools.db.org;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.inspectortools.db.org.OrgDbSchema.EmployeeTable;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class OrgDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "org.db";
    public static final String CREATE_EMPLOYEE = "create table "+
            EmployeeTable.NAME + "(_id integer primary key autoincrement," +
            EmployeeTable.Cols.PHONE+","+
            EmployeeTable.Cols.NAME+","+
            EmployeeTable.Cols.WORK_NUM+","+
            EmployeeTable.Cols.ORG_CODE+","+
            EmployeeTable.Cols.STATION_CODE+")";
    public OrgDbHelper(Context context, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
