package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;

import com.nisoft.inspectortools.bean.problem.Recode;

/**
 * Created by NewIdeaSoft on 2017/7/2.
 */

public class ProblemProgramFragment extends Fragment {
    private Recode mProgram;

    public Recode getProgram() {
        return mProgram;
    }

    public void setProgram(Recode program) {
        mProgram = program;
    }
}
