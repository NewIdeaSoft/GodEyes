package com.nisoft.inspectortools.ui.choosetype;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

public class ChooseRecodeTypeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ChooseRecodeTypeFragment();
    }
}
