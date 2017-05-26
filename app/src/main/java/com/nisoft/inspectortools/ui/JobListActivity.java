package com.nisoft.inspectortools.ui;


import android.app.Fragment;

public class JobListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new JobListFragment();
    }
}
