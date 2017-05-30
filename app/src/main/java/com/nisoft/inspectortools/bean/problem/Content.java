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
    public static final String[] CONTENT_TITLES = {"问题详情","原因分析","处理方案"};
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
        contents.add(new Content(CONTENT_TITLES[0],problem.getDetailedText(),problem.getDate(),problem.getDiscover()));
        contents.add(new Content(CONTENT_TITLES[1],problem.getReasonText(),problem.getAnalyzedDate(),problem.getAnalyst()));
        contents.add(new Content(CONTENT_TITLES[2],problem.getSolvedText(),problem.getSolvedDate(),problem.getSolver()));
        return contents;
    }
    public static void getProblem(ArrayList<Content> contents,Problem problem){
        for (int i=0;i<3;i++){
            Content content = contents.get(i);
            switch (i){
                case 0:
                    problem.setDiscover(content.getmAuthor());
                    problem.setDate(content.getmDate());
                    problem.setDetailedText(content.getmText());
                    break;
                case 1:
                    problem.setAnalyst(content.getmAuthor());
                    problem.setReasonText(content.getmText());
                    problem.setAnalyzedDate(content.getmDate());
                    break;
                case 2:
                    problem.setSolver(content.getmAuthor());
                    problem.setSolvedDate(content.getmDate());
                    problem.setSolvedText(content.getmText());
                    break;
            }
        }

    }
}
