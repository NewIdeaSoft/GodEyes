package com.nisoft.inspectortools.bean.problem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by NewIdeaSoft on 2017/4/24.
 */

public class Problem implements Serializable {
    private int _id;
    private UUID mUUID;
    private String mTitle;
    private Date mDate;
    private String mPosition;
    private String mDiscover;
    private ArrayList<String> mSuspects;
    private String mDetailedText;
    private ArrayList<String> mPhotoPath;
    private String mReasonText;
    private String mAnalyst;
    private Date mAnalyzedDate;
    private String mSolvedText;
    private String mSolver;
    private Date mSolvedDate;

    public Problem() {
        mUUID = UUID.randomUUID();
    }

    public Problem(UUID uuid) {
        mUUID = uuid;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date getSolvedDate() {
        return mSolvedDate;
    }

    public void setSolvedDate(Date solvedDate) {
        mSolvedDate = solvedDate;
    }

    public String getReasonText() {
        return mReasonText;
    }

    public void setReasonText(String reasonText) {
        mReasonText = reasonText;
    }

    public String getAnalyst() {
        return mAnalyst;
    }

    public void setAnalyst(String analyst) {
        mAnalyst = analyst;
    }

    public String getSolvedText() {
        return mSolvedText;
    }

    public void setSolvedText(String solvedText) {
        mSolvedText = solvedText;
    }

    public String getSolver() {
        return mSolver;
    }

    public void setSolver(String solver) {
        mSolver = solver;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        mPosition = position;
    }

    public String getDiscover() {
        return mDiscover;
    }

    public void setDiscover(String discover) {
        mDiscover = discover;
    }

    public ArrayList<String> getSuspects() {
        return mSuspects;
    }

    public void setSuspects(ArrayList<String> suspects) {
        mSuspects = suspects;
    }

    public String getDetailedText() {
        return mDetailedText;
    }

    public void setDetailedText(String detailedText) {
        mDetailedText = detailedText;
    }

    public ArrayList<String> getPhotoPath() {
        return mPhotoPath;
    }

    public void setPhotoPath(ArrayList<String> photoPath) {
        mPhotoPath = photoPath;
    }

    public Date getAnalyzedDate() {
        return mAnalyzedDate;
    }

    public void setAnalyzedDate(Date analyzedDate) {
        mAnalyzedDate = analyzedDate;
    }
}
