package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.KeyEvent;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

public class ProblemRecodeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String problemID = getIntent().getStringExtra(RecodeDbSchema.RecodeTable.Cols.PROBLEM_ID);
        return ProblemRecodeFragment1.newInstance(problemID);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            FragmentManager fm = getFragmentManager();
            ProblemRecodeFragment1 fragment = (ProblemRecodeFragment1) fm.findFragmentById(R.id.fragment_content);
            fragment.onKeyBackDown();
        }
        return super.onKeyDown(keyCode, event);
    }
}
