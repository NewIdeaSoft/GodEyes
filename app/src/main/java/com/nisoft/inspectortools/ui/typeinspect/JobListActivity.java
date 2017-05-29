package com.nisoft.inspectortools.ui.typeinspect;


import android.app.Fragment;

import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeFragment;

public class JobListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return JobListFragment.newInstance(getIntent().getStringExtra(ChooseRecodeTypeFragment.INSPECT_TYPE));
    }
}
