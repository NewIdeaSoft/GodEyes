package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

public class ProblemRecodeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String problemID = getIntent().getStringExtra(RecodeDbSchema.RecodeTable.Cols.PROBLEM_ID);
        return ProblemRecodeFragment1.newInstance(problemID);
    }
}
