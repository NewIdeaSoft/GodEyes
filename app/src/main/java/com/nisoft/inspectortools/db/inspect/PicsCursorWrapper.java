package com.nisoft.inspectortools.db.inspect;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.nisoft.inspectortools.bean.inspect.InspectRecodePics;
import com.nisoft.inspectortools.bean.inspect.PicsLab;

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

    public InspectRecodePics getPics(){
        InspectRecodePics pics = new InspectRecodePics();
        pics.setJobNum(getString(getColumnIndex(PicsDbSchema.PicTable.Cols.PIC_JOB_NUM)));
        pics.setDate(new Date(getLong(getColumnIndex(PicsDbSchema.PicTable.Cols.PIC_JOB_DATE))));

        pics.setPicPath(PicsLab.getStrings(getString(getColumnIndex(PicsDbSchema.PicTable.Cols.PICS))));


        return pics;
    }
}
