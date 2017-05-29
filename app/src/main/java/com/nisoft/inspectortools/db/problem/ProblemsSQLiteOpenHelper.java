package com.nisoft.inspectortools.db.problem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;

/**
 * Created by NewIdeaSoft on 2017/5/5.
 */

public class ProblemsSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "problemBase.db";
    private Context mContext;
    private static final String CREATE_PROBLEM = "create table "+ProblemTable.NAME+
            "(_id integer primary key autoincrement,"
            + ProblemTable.Cols.UUID+","
            + ProblemTable.Cols.TITLE+","
            + ProblemTable.Cols.POSITION+","
            + ProblemTable.Cols.DISCOVER+","
            + ProblemTable.Cols.DATE+","
            + ProblemTable.Cols.SUSPECTS+","
            + ProblemTable.Cols.DETAILED_TEXT+","
            + ProblemTable.Cols.PHOTO_PATH+","
            + ProblemTable.Cols.REASON_TEXT+","
            + ProblemTable.Cols.ANALYZED_DATE+","
            + ProblemTable.Cols.ANALYST+","
            + ProblemTable.Cols.SOLUTION+","
            + ProblemTable.Cols.HANDLER+","
            + ProblemTable.Cols.SOLVED_DATE+")";
    public ProblemsSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROBLEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
