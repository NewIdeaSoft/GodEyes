package com.nisoft.inspectortools.db.org;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class OrgDbSchema {
    public static class EmployeeTable{
        public static final String NAME="employee";
        public static class Cols{
            public static final String PHONE = "phone";
            public static final String NAME = "name";
            public static final String WORK_NUM = "work_num";
            public static final String ORG_CODE = "org_code";
            public static final String STATION_CODE = "station_code";
        }
    }
    public static class OrgTable{
        public static final String NAME ="org";
        public static class Cols{
            public static final String ORG_CODE = "org_code";
            public static final String ORG_NAME = "org_name";
            public static final String PARENT_CODE = "parent_code";
            public static final String ORG_LEVEL = "org_level";
        }
    }
}
