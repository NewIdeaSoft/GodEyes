package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Company;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.EmployeeDataPackage;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;
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
    private EditText mEmployeeNumEditText;
    private TextView mCompanyNameTextView;
    private ListView mOrgListView;
    private ListView mStationListView;
    private Button mDoneButton;
    private ProgressDialog mDialog;

    private String phone;
    private Employee mEmployee;
    private Company mCompany;
    private ArrayList<String> mOrgInfo;
    private ArrayList<String> mStationsInfo;
    private int mOrgLevels;

    private ArrayList<ArrayList<OrgInfo>> mOrgsForChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_info);
        init();
        initView();
    }

    private void init() {
        phone = getIntent().getStringExtra("phone");
        Gson gson  =  new Gson();
        mCompany = gson.fromJson(getIntent().getStringExtra("company"),Company.class);
        mOrgsForChoose = new ArrayList<>();
        downLoadInfo();
    }


    private void initView() {
        mDialog = new ProgressDialog(this);
        mNameEditText = (EditText) findViewById(R.id.et_name);
        mEmployeeNumEditText = (EditText) findViewById(R.id.et_member_num);
        mCompanyNameTextView = (TextView) findViewById(R.id.tv_company_name);
        mOrgListView = (ListView) findViewById(R.id.lv_org_info_item);
        mStationListView = (ListView) findViewById(R.id.lv_position_info);
        mDoneButton = (Button) findViewById(R.id.btn_register_done);

        mCompanyNameTextView.setText(mCompany.getOrgName());
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMoreInfo();
            }
        });


        mNameEditText.setText(mEmployee.getName());
        mEmployeeNumEditText.setText(mEmployee.getWorkNum());
    }

    private void downLoadInfo() {

        RequestBody body = new FormBody.Builder()
                .add("phone", phone)
                .build();
        String address = HttpUtil.ADRESS_MAIN + HttpUtil.SERVLET_USERINFO;
        DialogUtil.showProgressDialog(this, mDialog, "正在从服务器加载用户信息...");
        HttpUtil.sendPostRequest(address, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDialog.dismiss();
                Toast.makeText(MoreUserInfoActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();

                final EmployeeDataPackage dataPackage = gson.fromJson(result,EmployeeDataPackage.class);
                mEmployee = dataPackage.getEmployee();
                mOrgInfo = dataPackage.getOrgInfo();
                mStationsInfo = dataPackage.getStations();
                if (mOrgInfo==null||mOrgInfo.size()==0){
                    mOrgsForChoose.add(dataPackage.getOrgs());
                }
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
        DialogUtil.showProgressDialog(this, mDialog, "正在上传用户信息...");
        Gson gson =new Gson();
        String json = gson.toJson(mEmployee);
        RequestBody body = new FormBody.Builder()
                .add("employee",json)
                .build();
        String address = HttpUtil.ADRESS_MAIN + HttpUtil.SERVLET_USERINFO;
        HttpUtil.sendPostRequest(address, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(MoreUserInfoActivity.this, "网络链接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                if(s.equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Intent intent = new Intent(MoreUserInfoActivity.this, ChooseRecodeTypeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoreUserInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
