package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.EmployeeInfo;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoreUserInfoActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mEmproyeeNumEditText;
    private ListView mOrgListView;
    private ListView mStationListView;
    private Button mDoneButton;
    private ProgressDialog mDialog;

    private String phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_info);
        init();
        initView();
    }

    private void init(){
        phone = getIntent().getStringExtra("phone");
        downLoadInfo();
    }


    private void initView(){
        mDialog = new ProgressDialog(this);
        mNameEditText = (EditText) findViewById(R.id.et_name);
        mEmproyeeNumEditText = (EditText) findViewById(R.id.et_member_num);
        mOrgListView = (ListView) findViewById(R.id.lv_org_info_item);
        mStationListView = (ListView) findViewById(R.id.lv_position_info);
        mDoneButton = (Button) findViewById(R.id.btn_register_done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMoreInfo();
            }
        });
    }

    private void downLoadInfo() {

        RequestBody body = new FormBody.Builder()
                .add("phone",phone)
                .build();
        DialogUtil.showProgressDialog(this,mDialog,"正在从服务器加载用户信息...");
        HttpUtil.sendOkHttpRequest(HttpUtil.ADDRESS_USERINFO, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDialog.dismiss();
                Toast.makeText(MoreUserInfoActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final EmployeeInfo employee;
                final String name ;
                final String memberNum;
                final ArrayList<String> org;
                final ArrayList<String> positions;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                });
            }
        });
    }

    private void uploadMoreInfo() {
        DialogUtil.showProgressDialog(this,mDialog,"正在上传用户信息...");
        RequestBody body = new FormBody.Builder()
                .build();
        HttpUtil.sendOkHttpRequest(HttpUtil.ADDRESS_USERINFO, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
