package com.nisoft.inspectortools.bean.problem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nisoft.managertools.db.problem.ProblemsSQLiteOpenHelper;
import com.nisoft.managertools.db.problem.RecodeCursorWrapper;
import com.nisoft.managertools.db.problem.RecodeDbSchema.RecodeTable;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ProblemDataLab {
    private Context mContext;
    private static ProblemDataLab sProblemDataLab;
    private ProblemDataPackage mProblemDataPackage;
    private SQLiteDatabase mDatabase;

    private ProblemDataLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ProblemsSQLiteOpenHelper(mContext).getWritableDatabase();
    }
    public static ProblemDataLab getProblemDataLab(Context context){
        if(sProblemDataLab==null) {
            sProblemDataLab = new ProblemDataLab(context);
        }
        return sProblemDataLab;
    }

    public ProblemDataPackage getProblemDataPackage() {
        return mProblemDataPackage;
    }

    public void setProblemDataPackage(ProblemDataPackage problemDataPackage) {
        mProblemDataPackage = problemDataPackage;
    }

    private static ContentValues getImageValues(ImageRecode recode){
        ContentValues values = getRecodeValues(recode);
        String folderPath = recode.getImagesFolderPath();
        if(folderPath!=null) {
            values.put(RecodeTable.Cols.FOLDER_PATH,folderPath);
        }
        return values;
    }
    private static ContentValues getRecodeValues(Recode recode){
        ContentValues values = new ContentValues();
        String problemId = recode.getRecodeId();
        String type = recode.getType();
        String description = recode.getDescription();
        long dateTime = recode.getDate().getTime();
        long updateTime = recode.getUpdateTime();
        values.put(RecodeTable.Cols.PROBLEM_ID,problemId);
        values.put(RecodeTable.Cols.TYPE,type);
        values.put(RecodeTable.Cols.DESCRIPTION_TEXT,description);
        values.put(RecodeTable.Cols.DATE,dateTime);
        values.put(RecodeTable.Cols.UPDATE_TIME,updateTime);
        return values;
    }

    private static ContentValues getValues(ProblemRecode recode){
        ContentValues values = getImageValues(recode);
        String suspects = recode.getSuspects().toString();
        String address = recode.getAddress();
        String title = recode.getTitle();
        values.put(RecodeTable.Cols.TITLE,title);
        values.put(RecodeTable.Cols.SUSPECTS,suspects);
        values.put(RecodeTable.Cols.ADDRESS,address);
        return values;
    }
    private RecodeCursorWrapper queryRecode(String table,String selection,String[] args){
        Cursor cursor = mDatabase.query(table,
                null,
                selection,
                args,
                null,
                null,
                null);
        return new RecodeCursorWrapper(cursor);
    }

    public Recode queryRecode(String table,String problemId){
        Cursor cursor = mDatabase.query(table,
                null,
                RecodeTable.Cols.PROBLEM_ID+"=?",
                new String[]{problemId},
                null,
                null,
                null);
        RecodeCursorWrapper cursorWrapper = new RecodeCursorWrapper(cursor);
        switch (table) {
            case RecodeTable.PROBLEM_NAME :
                return cursorWrapper.getProblemRecode();
            case RecodeTable.PROGRAM_NAME:
                return cursorWrapper.getRecode();
            case RecodeTable.RESULT_NAME:
                return cursorWrapper.getImageRecode();
            case RecodeTable.ANALYSIS_NAME:
                return cursorWrapper.getRecode();
        }
        return cursorWrapper.getRecode();
    }

    public ArrayList<ProblemRecode> getAllProblem(){
        RecodeCursorWrapper wrapper = queryRecode(RecodeTable.PROBLEM_NAME,null,null);
        wrapper.moveToFirst();
        ArrayList<ProblemRecode> problems = new ArrayList<>();
        while (!wrapper.isAfterLast()){
            problems.add(wrapper.getProblemRecode());
        }
        return problems;
    }

    public ProblemDataPackage getProblemById(String problemId){
        ProblemDataPackage recode = new ProblemDataPackage(problemId);
        ProblemRecode problemRecode = (ProblemRecode) queryRecode(RecodeTable.PROBLEM_NAME,problemId);
        Recode analysis = queryRecode(RecodeTable.ANALYSIS_NAME,problemId);
        Recode program = queryRecode(RecodeTable.PROGRAM_NAME,problemId);
        ImageRecode result = (ImageRecode) queryRecode(RecodeTable.RESULT_NAME,problemId);
        if(problemRecode !=null) {
            recode.setProblem(problemRecode);
        }
        if(analysis!=null) {
            recode.setAnalysis(analysis);
        }
        if(program!=null) {
            recode.setProgram(program);
        }
        if(result!=null) {
            recode.setResultRecode(result);
        }
        return recode;
    }

    public void delete(ProblemRecode problem) {
        String whereClause = RecodeTable.Cols.PROBLEM_ID+"=?";
        String [] args = new String []{problem.getRecodeId() };
        mDatabase.delete(RecodeTable.PROBLEM_NAME,whereClause,args);
        mDatabase.delete(RecodeTable.ANALYSIS_NAME,whereClause,args);
        mDatabase.delete(RecodeTable.PROGRAM_NAME,whereClause,args);
        mDatabase.delete(RecodeTable.RESULT_NAME,whereClause,args);
    }

    public void addProblem(ProblemRecode problem) {

    }

    public void updateProblem(ProblemDataPackage problem) {
    }
}
