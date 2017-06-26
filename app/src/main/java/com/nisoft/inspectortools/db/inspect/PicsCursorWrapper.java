package com.nisoft.inspectortools.db.inspect;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;
import com.nisoft.inspectortools.db.inspect.PicsDbSchema.PicTable;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PicsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public MaterialInspectRecode getPics(){
        MaterialInspectRecode pics = new MaterialInspectRecode();
        pics.setJobNum(getString(getColumnIndex(PicTable.Cols.JOB_ID)));
        pics.setDate(new Date(getLong(getColumnIndex(PicTable.Cols.JOB_DATE))));
        pics.setPicFolderPath(getString(getColumnIndex(PicTable.Cols.FOLDER_PATH)));
        pics.setType(getString(getColumnIndex(PicTable.Cols.TYPE)));
        pics.setDescription(getString(getColumnIndex(PicTable.Cols.DESCRIPTION)));
        pics.setInspectorId(getString(getColumnIndex(PicTable.Cols.INSPECTOR_ID)));
        pics.setLatestUpdateTime(getLong(getColumnIndex(PicTable.Cols.UPDATE_TIME)));
        return pics;
    }
}
