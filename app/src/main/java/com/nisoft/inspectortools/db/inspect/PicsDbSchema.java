package com.nisoft.inspectortools.db.inspect;

/**
 * Created by Administrator on 2017/5/22.
 */

public class PicsDbSchema {
    public static final class PicTable{
        public static final String NAME= "recode_pic";
        public static final class Cols{
            public static final String JOB_ID = "job_id";
            public static final String JOB_DATE = "date";
            public static final String TYPE = "job_type";
            public static final String DESCRIPTION = "description";
            public static final String FOLDER_PATH = "folder";
            public static final String INSPECTOR_ID = "inspector_id";
        }

    }
}
