package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;


import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

import java.util.UUID;

public class ProblemRecodeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ProblemTable.Cols.UUID);
        return ProblemRecodeFragment.newInstance(uuid);
    }
}
