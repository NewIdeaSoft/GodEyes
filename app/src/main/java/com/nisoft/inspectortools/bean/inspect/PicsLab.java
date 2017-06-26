package com.nisoft.inspectortools.bean.inspect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nisoft.inspectortools.db.inspect.PicsCursorWrapper;
import com.nisoft.inspectortools.db.inspect.PicsDbHelper;
import com.nisoft.inspectortools.db.inspect.PicsDbSchema.PicTable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsLab {
    private static PicsLab sPicsLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private PicsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = (new PicsDbHelper(mContext, 2)).getWritableDatabase();
    }

    public static PicsLab getPicsLab(Context context) {
        if (sPicsLab == null) {
            sPicsLab = new PicsLab(context.getApplicationContext());
        }
        return sPicsLab;
    }

    public PicsCursorWrapper queryPics(String whereClause, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(PicTable.NAME, null, whereClause, selectionArgs, null, null, PicTable.Cols.JOB_ID);
//        if (cursor==null){return null;}
        return new PicsCursorWrapper(cursor);
    }

    public ArrayList<MaterialInspectRecode> getAllPics() {
        PicsCursorWrapper cursorWrapper = queryPics(null, null);
        ArrayList<MaterialInspectRecode> pics = new ArrayList<>();
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                pics.add(cursorWrapper.getPics());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return pics;
    }

    public ArrayList<MaterialInspectRecode> getAllPics(PicsCursorWrapper cursorWrapper) {
        ArrayList<MaterialInspectRecode> pics = new ArrayList<>();
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                pics.add(cursorWrapper.getPics());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return pics;
    }

    public static ContentValues getPicsContentValues(MaterialInspectRecode pics) {
        ContentValues contentValues = new ContentValues();
        if (pics.getJobNum() != null) {
            contentValues.put(PicTable.Cols.JOB_ID, pics.getJobNum());
        }
        if (pics.getDate() != null) {
            contentValues.put(PicTable.Cols.JOB_DATE, pics.getDate().getTime());
        }
        if (pics.getType() != null) {
            contentValues.put(PicTable.Cols.TYPE, pics.getType());
        }
        if (pics.getInspectorId()!=null){
            contentValues.put(PicTable.Cols.INSPECTOR_ID,pics.getInspectorId());
        }
        if (pics.getDescription() != null) {
            contentValues.put(PicTable.Cols.DESCRIPTION, pics.getDescription());
        }
        contentValues.put(PicTable.Cols.UPDATE_TIME, pics.getLatestUpdateTime());
        return contentValues;
    }

    public void updatePics(MaterialInspectRecode pics, String oldJobNum) {
        ContentValues contentValues = getPicsContentValues(pics);
        if (contentValues.size() > 0) {
            if (pics.getJobNum() == null) {
                return;
            }
            PicsCursorWrapper cursorWrapper = queryPics(PicTable.Cols.JOB_ID + "=?", new String[]{oldJobNum});
            Log.e("TAG", cursorWrapper.getCount() + "");
            if (cursorWrapper != null && cursorWrapper.getCount() > 0) {
                mDatabase.update(PicTable.NAME, contentValues, PicTable.Cols.JOB_ID + "=?", new String[]{oldJobNum});
                Log.e("TAG", "update:" + pics.getJobNum());
            } else {
                mDatabase.insert(PicTable.NAME, null, contentValues);
                Log.e("TAG", "insert:" + pics.getJobNum());
            }
            cursorWrapper.close();
        }
    }

    public void insertPics(MaterialInspectRecode pics) {
        ContentValues contentValues = getPicsContentValues(pics);
        mDatabase.insert(PicTable.NAME, null, contentValues);
    }

    public MaterialInspectRecode getPicsByJobNum(String mJobNum) {
        PicsCursorWrapper cursorWrapper = queryPics(PicTable.Cols.JOB_ID + "=?", new String[]{mJobNum});
        if (cursorWrapper == null) {
            return null;
        }
        MaterialInspectRecode pics = null;
        try {
            cursorWrapper.moveToFirst();
            pics = cursorWrapper.getPics();
        } finally {
            cursorWrapper.close();
        }
        return pics;
    }

    public PicsCursorWrapper queryPicsByTwo(String jobType, String jobNum) {
        String sql = "select * from " + PicTable.NAME + " where("
                + PicTable.Cols.TYPE + " like '" + jobType
                + "')and(" + PicTable.Cols.JOB_ID + " like '%" + jobNum + "%')";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        return new PicsCursorWrapper(cursor);
    }

    public void changeJobId(String newId, String oldId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PicTable.Cols.JOB_ID, newId);
        mDatabase.update(PicTable.NAME, contentValues, PicTable.Cols.JOB_ID + "=?", new String[]{oldId});
    }
}
