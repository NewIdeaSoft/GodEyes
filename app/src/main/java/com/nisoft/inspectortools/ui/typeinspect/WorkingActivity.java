package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.KeyEvent;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.KEY_SELECTED_TYPE;

/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingActivity extends SingleFragmentActivity {
    private String mJobNum;

    @Override
    protected Fragment createFragment() {
        mJobNum = getIntent().getStringExtra("job_num");
        boolean isNewJob = getIntent().getBooleanExtra("isNewJob", false);
        int whichType = getIntent().getIntExtra(KEY_SELECTED_TYPE, -1);
        return WorkingFragment.newInstance(mJobNum, whichType, isNewJob);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            FragmentManager fm = getFragmentManager();
            WorkingFragment fragment = (WorkingFragment) fm.findFragmentById(R.id.fragment_content);
            fragment.onKeyBackDown();
        }
        return super.onKeyDown(keyCode, event);
    }
}
