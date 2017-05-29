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
    private PicsLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = (new PicsDbHelper(mContext)).getWritableDatabase();
    }
    public static PicsLab getPicsLab(Context context){
        if (sPicsLab==null){
            sPicsLab = new PicsLab(context.getApplicationContext());
        }
        return sPicsLab;
    }

    public PicsCursorWrapper queryPics(String whereClause,String [] selectionArgs){
        Cursor cursor = mDatabase.query(PicTable.NAME,null,whereClause,selectionArgs,null,null,null);
//        if (cursor==null){return null;}
        return new PicsCursorWrapper(cursor);
    }
    public static ArrayList<String> getStrings(String s) {
        ArrayList<String> strings = new ArrayList<>();
        if (s != null) {
            String[] ss = s.split(",");
            for (String s1 : ss) {
                s1 = s1.trim();
                strings.add(s1);
            }
        }
        return strings;
    }

    public static String arrayListToString(ArrayList<String> strings) {
        if (strings == null || strings.size() == 0) {
            return null;
        }
        String s = strings.toString();
        return s.substring(1, s.length() - 1);
    }

    public ArrayList<InspectRecodePics> getAllPics(){
        PicsCursorWrapper cursorWrapper = queryPics(null,null);
        ArrayList<InspectRecodePics> pics = new ArrayList<>();
        try{
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                pics.add(cursorWrapper.getPics());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }

        return pics;
    }
    public static ContentValues getPicsContentValues(InspectRecodePics pics){
        ContentValues contentValues = new ContentValues();
        if (pics.getJobNum()!=null){
            contentValues.put(PicTable.Cols.PIC_JOB_NUM,pics.getJobNum());
        }
        contentValues.put(PicTable.Cols.PIC_JOB_DATE,pics.getDate().getTime());
        if (pics.getPicPath()!=null&&pics.getPicPath().size()>0){
            contentValues.put(PicTable.Cols.PICS,arrayListToString(pics.getPicPath()));
        }
        return contentValues;
    }

    public void updatePics (InspectRecodePics pics,String oldJobNum){
        ContentValues contentValues = getPicsContentValues(pics);
        if (contentValues.size()>0){
            if (pics.getJobNum() == null){
                return;
            }
            PicsCursorWrapper cursorWrapper = queryPics(PicTable.Cols.PIC_JOB_NUM+"=?",new String[]{oldJobNum});
            Log.e("TAG",cursorWrapper.getCount()+"");
            if (cursorWrapper!=null&&cursorWrapper.getCount()>0){
                mDatabase.update(PicTable.NAME,contentValues,PicTable.Cols.PIC_JOB_NUM+"=?",new String[]{oldJobNum});
                Log.e("TAG","update:"+pics.getJobNum());
            }else{
                mDatabase.insert(PicTable.NAME,null,contentValues);
                Log.e("TAG","insert:"+pics.getJobNum());
            }
            cursorWrapper.close();

        }
    }
    public InspectRecodePics getPicsByJobNum(String mJobNum){
        PicsCursorWrapper cursorWrapper = queryPics(PicTable.Cols.PIC_JOB_NUM+"=?",new String[]{mJobNum});
        if (cursorWrapper==null){
            return null;
        }
        InspectRecodePics pics = null;
        try{
            cursorWrapper.moveToFirst();
            pics = cursorWrapper.getPics();
        }finally {
            cursorWrapper.close();
        }
        return pics;
    }
}
