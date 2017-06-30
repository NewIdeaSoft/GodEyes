package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;

import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

public class ProblemListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ProblemListFragment();
    }
}
