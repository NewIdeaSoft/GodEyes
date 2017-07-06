package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Recode;

/**
 * Created by NewIdeaSoft on 2017/7/2.
 */

public class ProblemProgramFragment extends RecodeFragment {
    public static final int REQUEST_PROGRAM_AUTHOR = 301;
    public static final int REQUEST_PROGRAM_DATE = 302;
    public static final int REQUEST_PROGRAM_DESCRIPTION = 303;
    private Recode mProgram;

    public Recode getProgram() {
        return mProgram;
    }

    public void setProgram(Recode program) {
        mProgram = program;
    }

    @Override
    protected void init() {
        mProgram = ProblemRecodeFragment1.getProblem().getProgram();
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setProgram(mProgram);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_program,container,false);
        return view;
    }

}
