package com.nisoft.inspectortools.db.problem;

/**
 * Created by NewIdeaSoft on 2017/7/1.
 */

public class RecodeDbSchema {
    public static final class RecodeTable {
        public static final String PROGRAM_NAME = "program";
        public static final String RESULT_NAME = "result";
        public static final String PROBLEM_NAME = "problem";
        public static final String ANALYSIS_NAME = "analysis";
        public static final class Cols{
            public static final String PROBLEM_ID = "problem_id";
            public static final String DATE = "date";
            public static final String AUTHOR = "author";
            public static final String DESCRIPTION_TEXT ="description";
            public static final String FOLDER_PATH = "folder";
            public static final String UPDATE_TIME = "update_time";
            public static final String TYPE = "type";
            public static final String SUSPECTS = "suspects";
            public static final String TITLE="title";
            public static final String ADDRESS = "address";
        }
    }
}
