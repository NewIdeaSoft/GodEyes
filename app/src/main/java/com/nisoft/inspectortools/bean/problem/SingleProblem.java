package com.nisoft.inspectortools.bean.problem;

import android.content.Context;

/**
 * Created by NewIdeaSoft on 2017/5/14.
 */

public class SingleProblem {
    private static SingleProblem singleProblem;
    private Problem mProblem;
    private Context mContext;
    public static SingleProblem getSingleProblem(Context context){
        if(singleProblem==null) {
            singleProblem = new SingleProblem(context);
        }
        return singleProblem;
    }
    private SingleProblem(Context context){
        mContext = context;
    }

    public Problem getProblem() {
        return mProblem;
    }

    public void setProblem(Problem problem) {
        mProblem = problem;
    }
}
