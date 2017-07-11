package com.nisoft.inspectortools.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/29.
 */

public class StringFormatUtil {
    public static ArrayList<String> getStrings(String s) {
        ArrayList<String> strings = new ArrayList<>();
        if (s != null && !s.equals("[]")) {
            if (s.startsWith("[")) {
                s = s.substring(1, s.length() - 1);
            }
            String[] ss = s.split(",");
            for (String s1 : ss) {
                s1 = s1.trim();
                strings.add(s1);
            }

        }
        return strings;
    }

    public static ArrayList<String> getJsonStrings(String s) {
        ArrayList<String> strings = new ArrayList<>();
        if (s != null && !s.equals("[]")) {
            if (s.startsWith("[")) {
                s = s.substring(1, s.length() - 1);
            }
            s.trim();
            String split = "}";
            String[] ss = s.split(split);
            for (String s1 : ss) {
                if (!s1.endsWith("}")) {
                    s1 = s1 + "}";
                }
                strings.add(s1);
            }

        }
        return strings;
    }

    public static String arrayListToString(ArrayList<String> strings) {
        if (strings == null || strings.size() == 0) {
            return null;
        }
        String s = strings.toString();
        return s.substring(1, s.length() - 1);
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = format.format(date);
        return dateString;
    }

    public static String correctString(String s) {
        if (s.equals("null")) {
            s = "";
        }
        return s;
    }
}
