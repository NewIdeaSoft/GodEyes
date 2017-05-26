package com.nisoft.inspectortools.ui;

import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.nisoft.inspectortools.R;

/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingActivity extends SingleFragmentActivity{
    private String mJobNum;
    @Override
    protected Fragment createFragment() {
        mJobNum = getIntent().getStringExtra("job_num");
        return WorkingFragment.newInstance(mJobNum);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            String jobNum = WorkingFragment.getsRecodePics().getJobNum();
            EditText editText = ((WorkingFragment)getFragmentManager().findFragmentById(R.id.fragment_content)).getJobNumber();

            if (editText.isFocusable()||jobNum==null||jobNum.length()<6){
                Snackbar.make(findViewById(R.id.fragment_content),"未输入或确认检验编号，无法保存数据，确定退出吗？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
