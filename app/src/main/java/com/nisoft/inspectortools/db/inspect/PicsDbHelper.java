package com.nisoft.inspectortools.db.inspect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "inspector_tools.db";
    private static final int VERSION = 1;
    private static final String CREATE_TABLE = "create table "
            + PicsDbSchema.PicTable.NAME +"( _id integer primary key autoincrement,"
            + PicsDbSchema.PicTable.Cols.PIC_JOB_NUM +","
            + PicsDbSchema.PicTable.Cols.PICS + ","
            + PicsDbSchema.PicTable.Cols.PIC_JOB_DATE+")";
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
