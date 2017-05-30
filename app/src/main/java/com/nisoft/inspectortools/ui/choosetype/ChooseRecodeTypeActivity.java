package com.nisoft.inspectortools.ui.choosetype;

import android.app.Fragment;

import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

public class ChooseRecodeTypeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ChooseRecodeTypeFragment();
    }
}
