package com.nisoft.inspectortools.ui.settings;

import android.app.Fragment;
import android.content.Intent;

import com.nisoft.inspectortools.ui.base.SingleFragmentActivity;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ContactsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        String companyId = getIntent().getStringExtra("company_id");
        return ContactsFragment.newInstance(companyId);
    }
}
