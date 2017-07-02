package com.nisoft.inspectortools.db.problem;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.managertools.db.problem.RecodeDbSchema.RecodeTable;
import com.nisoft.managertools.entity.problem.ImageRecode;
import com.nisoft.managertools.entity.problem.ProblemRecode;
import com.nisoft.managertools.entity.problem.Recode;
import com.nisoft.managertools.utils.StringFormatUtil;

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
        ImageRecode imageRecode = (ImageRecode) getRecode();
        String folderPath = getString(getColumnIndex(RecodeTable.Cols.FOLDER_PATH));
        imageRecode.setImagesFolderPath(folderPath);
        return imageRecode;
    }

    public ProblemRecode getProblemRecode() {
        ProblemRecode recode = (ProblemRecode) getImageRecode();
        String suspects = getString(getColumnIndex(RecodeTable.Cols.SUSPECTS));
        String title = getString(getColumnIndex(RecodeTable.Cols.TITLE));
        String address = getString(getColumnIndex(RecodeTable.Cols.ADDRESS));
        recode.setAddress(address);
        recode.setSuspects(StringFormatUtil.getStrings(suspects));
        recode.setTitle(title);
        return recode;
    }

}
