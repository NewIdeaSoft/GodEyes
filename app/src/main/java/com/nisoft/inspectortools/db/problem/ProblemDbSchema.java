package com.nisoft.inspectortools.db.problem;

/**
 * Created by NewIdeaSoft on 2017/5/11.
 */

public class ProblemDbSchema {
    public static final class ProblemTable{
        public static final String NAME = "problems";
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE="title";
            public static final String DATE = "date";
            public static final String POSITION = "position";
            public static final String DISCOVER = "discover";
            public static final String SUSPECTS = "suspects";
            public static final String DETAILED_TEXT = "detailed_text";
            public static final String PHOTO_PATH = "photo_path";
            public static final String REASON_TEXT = "reason_text";
            public static final String ANALYST = "analyst";
            public static final String SOLUTION = "solution";
            public static final String HANDLER = "handler";
            public static final String SOLVED_DATE = "solved_date";
            public static final String ID = "_id";
            public static final String ANALYZED_DATE = "analyzed_date";
        }
    }
}
