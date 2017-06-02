package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;
import com.nisoft.inspectortools.ui.typeinspect.WorkingFragment;

import java.util.UUID;

public class ProblemRecodeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(ProblemTable.Cols.UUID);
        return ProblemRecodeFragment.newInstance(uuid);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            String title = ProblemRecodeFragment.getProblem().getTitle();
            if (title==null){
                Snackbar.make(findViewById(R.id.fragment_content),"为输入主题，数据将无法保存，确定退出吗",Snackbar.LENGTH_LONG)
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
