package com.nisoft.inspectortools.bean.problem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nisoft.inspectortools.db.problem.ProblemCursorWrapper;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.db.problem.ProblemsSQLiteOpenHelper;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by NewIdeaSoft on 2017/5/5.
 */

public class ProblemLab {
    private static ProblemLab sProblemLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static ContentValues getContentValues(Problem problem) {
        ContentValues values = new ContentValues();
        values.put(ProblemTable.Cols.UUID, problem.getUUID().toString());
        values.put(ProblemTable.Cols.TITLE, problem.getTitle());
        if (problem.getDate() != null) {
            values.put(ProblemTable.Cols.DATE, problem.getDate().getTime());
        }
        if (problem.getPosition() != null) {
            values.put(ProblemTable.Cols.POSITION, problem.getPosition());
        }
        if (problem.getDiscover() != null) {
            values.put(ProblemTable.Cols.DISCOVER, problem.getDiscover());
        }
        if (problem.getSuspects() != null) {
            values.put(ProblemTable.Cols.SUSPECTS, StringFormatUtil.arrayListToString(problem.getSuspects()));
        }
        if (problem.getDetailedText() != null) {
            values.put(ProblemTable.Cols.DETAILED_TEXT, problem.getDetailedText());
        }
        if (problem.getPhotoPath() != null) {
            values.put(ProblemTable.Cols.PHOTO_PATH, StringFormatUtil.arrayListToString(problem.getPhotoPath()));
        }
        if (problem.getReasonText() != null) {
            values.put(ProblemTable.Cols.REASON_TEXT, problem.getReasonText());
        }
        if (problem.getAnalyst() != null) {
            values.put(ProblemTable.Cols.ANALYST, problem.getAnalyst());
        }
        if (problem.getSolvedText() != null) {
            values.put(ProblemTable.Cols.SOLUTION, problem.getSolvedText());
        }
        if (problem.getSolver() != null) {
            values.put(ProblemTable.Cols.HANDLER, problem.getSolver());
        }
        if (problem.getSolvedDate() != null) {
            values.put(ProblemTable.Cols.SOLVED_DATE, problem.getSolvedDate().getTime());
        }
        if (problem.getAnalyzedDate()!=null){
            values.put(ProblemTable.Cols.ANALYZED_DATE,problem.getAnalyzedDate().getTime());
        }
        return values;
    }

    private ProblemCursorWrapper queryProblem(String whereClause, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(ProblemTable.NAME, null, whereClause, selectionArgs, null, null, null);
        return new ProblemCursorWrapper(cursor);
    }


    private ProblemLab(Context context) {
        mContext = context.getApplicationContext();
        //从数据库加载problems
        mDatabase = (new ProblemsSQLiteOpenHelper(mContext)).getWritableDatabase();
    }

    public ArrayList<Problem> getProblems() {
        ArrayList<Problem> problems = new ArrayList<>();
        ProblemCursorWrapper cursor = queryProblem(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                problems.add(cursor.getProblem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return problems;
    }

    public static ProblemLab getProblemLab(Context context) {
        if (sProblemLab == null) {
            sProblemLab = new ProblemLab(context.getApplicationContext());
        }
        return sProblemLab;
    }

    public Problem getProblem(UUID id) {
        ProblemCursorWrapper cursor = queryProblem(ProblemTable.Cols.UUID + "=?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProblem();
        } finally {
            cursor.close();
        }
    }

    public void addProblem(Problem problem) {
        ContentValues values = getContentValues(problem);
        mDatabase.insert(ProblemTable.NAME, null, values);
    }

    public void updateProblem(Problem problem) {
        String uuidString = problem.getUUID().toString();
        ContentValues values = getContentValues(problem);
        mDatabase.update(ProblemTable.NAME, values, ProblemTable.Cols.UUID + "=?", new String[]{uuidString});
    }

    public void delete(Problem problem) {
        UUID uuid = problem.getUUID();
        mDatabase.delete(ProblemTable.NAME,ProblemTable.Cols.UUID+"=?",new String[]{uuid.toString()});
    }
}
