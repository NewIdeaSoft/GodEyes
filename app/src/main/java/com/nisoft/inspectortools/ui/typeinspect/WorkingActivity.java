package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeFragment;

/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingActivity extends SingleFragmentActivity {
    private String mJobNum;

    @Override
    protected Fragment createFragment() {
        mJobNum = getIntent().getStringExtra("job_num");
        boolean isNewJob = getIntent().getBooleanExtra("isNewJob", false);
        String inspectType = getIntent().getStringExtra(ChooseRecodeTypeFragment.INSPECT_TYPE);
        return WorkingFragment.newInstance(mJobNum, inspectType, isNewJob);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            String jobNum = WorkingFragment.getsRecodePics().getJobNum();
//            Snackbar.make(findViewById(R.id.fragment_content), "数据未上传，确定退出吗？", Snackbar.LENGTH_LONG)
//                    .setAction("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            finish();
//                        }
//                    }).show();
//        }
        return super.onKeyDown(keyCode, event);
    }
}
