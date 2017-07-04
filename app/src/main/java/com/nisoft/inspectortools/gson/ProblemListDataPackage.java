package com.nisoft.inspectortools.gson;

import com.nisoft.inspectortools.bean.problem.ProblemRecode;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/4.
 */

public class ProblemListDataPackage {
    private ArrayList<ProblemRecode> mProblemRecodes;

    public ArrayList<ProblemRecode> getProblemRecodes() {
        return mProblemRecodes;
    }

    public void setProblemRecodes(ArrayList<ProblemRecode> problemRecodes) {
        mProblemRecodes = problemRecodes;
    }
}
