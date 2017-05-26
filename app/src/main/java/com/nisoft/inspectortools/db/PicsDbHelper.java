package com.nisoft.inspectortools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.inspectortools.db.PicsDbSchema.PicTable;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "inspector_tools.db";
    private static final int VERSION = 1;
    private static final String CREATE_TABLE = "create table "
            + PicTable.NAME +"( _id integer primary key autoincrement,"
            + PicTable.Cols.PIC_JOB_NUM +","
            + PicTable.Cols.PICS + ","
            + PicTable.Cols.PIC_JOB_DATE+")";
    public PicsDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
