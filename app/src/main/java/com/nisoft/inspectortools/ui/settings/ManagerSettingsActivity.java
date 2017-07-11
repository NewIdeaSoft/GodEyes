package com.nisoft.inspectortools.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nisoft.inspectortools.R;

/**
 * 管理设置
 * 包括：组织机构设置，成员岗位信息修改
 */
public class ManagerSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_structrue_setting);
    }
}
