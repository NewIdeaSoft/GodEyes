package com.nisoft.inspectortools.bean.problem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */

public class Content {
    private String mTitle;
    private String mText;
    private Date mDate;
    private String mAuthor;

    public Content(String mTitle, String mText, Date mDate, String mAuthor) {
        this.mTitle = mTitle;
        this.mText = mText;
        this.mDate = mDate;
        this.mAuthor = mAuthor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }
    public static ArrayList<Content> getContentsFromProblem(Problem problem){
        ArrayList<Content> contents = new ArrayList<>();
        contents.add(new Content("问题详情",problem.getDetailedText(),problem.getDate(),problem.getDiscover()));
        contents.add(new Content("问题分析",problem.getReasonText(),problem.getAnalyzedDate(),problem.getAnalyst()));
        contents.add(new Content("处理方案",problem.getSolvedText(),problem.getSolvedDate(),problem.getSolver()));
        return contents;
    }
}
