package com.nisoft.inspectortools.db.inspect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.inspectortools.db.inspect.PicsDbSchema.PicTable;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsDbHelper extends SQLiteOpenHelper {
    private static final String NAME = "inspector_tools.db";
    private static final String CREATE_TABLE = "create table "
            + PicTable.NAME + "( _id integer primary key autoincrement,"
            + PicTable.Cols.PIC_JOB_NUM + ","
            + PicTable.Cols.PICS + ","
            + PicTable.Cols.FOLDER_PATH + ","
            + PicTable.Cols.PIC_JOB_DATE + ","
            + PicTable.Cols.TYPE + ","
            + PicTable.Cols.DESCRIPTION + ")";

    public PicsDbHelper(Context context, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("alter table " + PicTable.NAME + " add column " + PicTable.Cols.TYPE + " text");
//        db.execSQL("alter table " + PicTable.NAME + " add column " + PicTable.Cols.DESCRIPTION + "text");
    }
}
