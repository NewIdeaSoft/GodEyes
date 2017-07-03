package com.nisoft.inspectortools.db.problem;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.problem.ImageRecode;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.bean.problem.Recode;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class RecodeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public RecodeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recode getRecode(){
        String problemId = getString(getColumnIndex(RecodeTable.Cols.PROBLEM_ID));
        String author = getString(getColumnIndex(RecodeTable.Cols.AUTHOR));
        String type = getString(getColumnIndex(RecodeTable.Cols.TYPE));
        Date date = new Date(getLong(getColumnIndex(RecodeTable.Cols.DATE)));
        String descriptionText = getString(getColumnIndex(RecodeTable.Cols.DESCRIPTION_TEXT));
        long update_time = getLong(getColumnIndex(RecodeTable.Cols.UPDATE_TIME));
        Recode recode = new Recode(problemId);
        recode.setDate(date);
        recode.setAuthor(author);
        recode.setType(type);
        recode.setUpdateTime(update_time);
        recode.setDescription(descriptionText);
        return recode;
    }

    public ImageRecode getImageRecode(){
        String problemId = getString(getColumnIndex(RecodeTable.Cols.PROBLEM_ID));
        String author = getString(getColumnIndex(RecodeTable.Cols.AUTHOR));
        String type = getString(getColumnIndex(RecodeTable.Cols.TYPE));
        Date date = new Date(getLong(getColumnIndex(RecodeTable.Cols.DATE)));
        String descriptionText = getString(getColumnIndex(RecodeTable.Cols.DESCRIPTION_TEXT));
        long update_time = getLong(getColumnIndex(RecodeTable.Cols.UPDATE_TIME));
        String folderPath = getString(getColumnIndex(RecodeTable.Cols.FOLDER_PATH));
        ImageRecode recode = new ImageRecode(problemId);
        recode.setDate(date);
        recode.setAuthor(author);
        recode.setType(type);
        recode.setUpdateTime(update_time);
        recode.setDescription(descriptionText);
        recode.setImagesFolderPath(folderPath);
        return recode;
    }

    public ProblemRecode getProblemRecode() {
        String problemId = getString(getColumnIndex(RecodeTable.Cols.PROBLEM_ID));
        String author = getString(getColumnIndex(RecodeTable.Cols.AUTHOR));
        String type = getString(getColumnIndex(RecodeTable.Cols.TYPE));
        Date date = new Date(getLong(getColumnIndex(RecodeTable.Cols.DATE)));
        String descriptionText = getString(getColumnIndex(RecodeTable.Cols.DESCRIPTION_TEXT));
        long update_time = getLong(getColumnIndex(RecodeTable.Cols.UPDATE_TIME));
        String folderPath = getString(getColumnIndex(RecodeTable.Cols.FOLDER_PATH));
        String suspects = getString(getColumnIndex(RecodeTable.Cols.SUSPECTS));
        String title = getString(getColumnIndex(RecodeTable.Cols.TITLE));
        String address = getString(getColumnIndex(RecodeTable.Cols.ADDRESS));
        ProblemRecode recode = new ProblemRecode(problemId);
        recode.setDate(date);
        recode.setAuthor(author);
        recode.setType(type);
        recode.setUpdateTime(update_time);
        recode.setDescription(descriptionText);
        recode.setImagesFolderPath(folderPath);
        recode.setAddress(address);
        recode.setSuspects(StringFormatUtil.getStrings(suspects));
        recode.setTitle(title);
        return recode;
    }

}
