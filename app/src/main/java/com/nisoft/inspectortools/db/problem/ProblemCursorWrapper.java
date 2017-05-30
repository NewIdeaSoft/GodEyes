package com.nisoft.inspectortools.db.problem;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.problem.Problem;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by NewIdeaSoft on 2017/5/11.
 */

public class ProblemCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProblemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Problem getProblem(){
        UUID uuid = UUID.fromString(getString(getColumnIndex(ProblemTable.Cols.UUID)));
        Problem p = new Problem(uuid);
        String title = getString(getColumnIndex(ProblemTable.Cols.TITLE));
        p.setTitle(title);
        Date date =  new Date(getLong(getColumnIndex(ProblemTable.Cols.DATE)));
        p.setDate(date);
        String discover = getString(getColumnIndex(ProblemTable.Cols.DISCOVER));
        p.setDiscover(discover);
        String position = getString(getColumnIndex(ProblemTable.Cols.POSITION));
        p.setPosition(position);
        ArrayList<String> suspects  = StringFormatUtil.getStrings(getString(getColumnIndex(ProblemTable.Cols.SUSPECTS)));
        p.setSuspects(suspects);
        String detailed_text = getString(getColumnIndex(ProblemTable.Cols.DETAILED_TEXT));
        p.setDetailedText(detailed_text);
        ArrayList<String> photo_path = StringFormatUtil.getStrings(getString(getColumnIndex(ProblemTable.Cols.PHOTO_PATH)));
        p.setPhotoPath(photo_path);
        String reason_text = getString(getColumnIndex(ProblemTable.Cols.REASON_TEXT));
        p.setReasonText(reason_text);
        String analyst = getString(getColumnIndex(ProblemTable.Cols.ANALYST));
        p.setAnalyst(analyst);
        String solution = getString(getColumnIndex(ProblemTable.Cols.SOLUTION));
        p.setSolvedText(solution);
        String handler = getString(getColumnIndex(ProblemTable.Cols.HANDLER));
        p.setSolver(handler);
        Date solved_date = new Date(getLong(getColumnIndex(ProblemTable.Cols.SOLVED_DATE)));
        p.setSolvedDate(solved_date);
        Date analyzed_date = new Date(getLong(getColumnIndex(ProblemTable.Cols.ANALYZED_DATE)));
        p.setAnalyzedDate(analyzed_date);
        return p;
    }
}
