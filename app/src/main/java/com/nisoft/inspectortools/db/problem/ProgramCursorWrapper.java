package com.nisoft.inspectortools.db.problem;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class ProgramCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ProgramCursorWrapper(Cursor cursor) {
        super(cursor);
    }

}
