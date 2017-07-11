package com.nisoft.inspectortools.ui.typeinspect;


import android.app.Fragment;

import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.KEY_SELECTED_TYPE;

public class JobListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return JobListFragment.newInstance(getIntent().getIntExtra(KEY_SELECTED_TYPE, -1));
    }
}
