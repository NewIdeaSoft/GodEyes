package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Company;
import com.nisoft.inspectortools.bean.org.RegisterDataPackage;
import com.nisoft.inspectortools.utils.CheckUserInfoUtil;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {

    private Button mDoneButton;
    private EditText mPhoneEditText;
    private EditText mPasswordEditText;
    private EditText mCheckedPasswordEditText;
    private ProgressDialog mDialog;
    private Spinner mSpinner;

    private String initPhone;
    private ArrayList<Company> mAllCompanies;


    private ArrayAdapter<Company> mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        initView();
    }

    private void init() {
        mDialog = new ProgressDialog(this);
        initPhone = getIntent().getStringExtra(LoginActivity.PHONE);
        getCompaniesFromServer();
        mAdapter = new ArrayAdapter<Company>(this,android.R.layout.simple_spinner_item){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                super.getView(position, convertView, parent);
                convertView = View.inflate(RegisterActivity.this,android.R.layout.simple_spinner_item,null);
                TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
                textView.setText(mAllCompanies.get(position).getOrgName());
                return convertView;
            }
        };
    }

    private void getCompaniesFromServer() {
        DialogUtil.showProgressDialog(this,mDialog,"正在从服务器加载组织信息...");
        HttpUtil.sendGetRequest(LoginActivity.ADDRESS_LOGIN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "连接服务器失败！请检查网络连接。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                mAllCompanies = gson.fromJson(result, RegisterDataPackage.class).getCompanies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                });
            }
        });
    }

    private void initView() {
        mPhoneEditText = (EditText) findViewById(R.id.et_phone);
        if (initPhone!=null){
            mPhoneEditText.setText(initPhone);
        }
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mCheckedPasswordEditText = (EditText) findViewById(R.id.et_checked_password);
        mSpinner = (Spinner) findViewById(R.id.spinner_company);
        mSpinner.setAdapter(mAdapter);
        mDoneButton = (Button) findViewById(R.id.btn_register_done);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone = mPhoneEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String checkedPassword = mCheckedPasswordEditText.getText().toString();
                if (!CheckUserInfoUtil.checkPhoneFormat(userPhone)){
                    Toast.makeText(RegisterActivity.this, "您输入的不是手机号，请重新输入！", Toast.LENGTH_SHORT).show();
                    mPhoneEditText.setText("");
                    mPhoneEditText.setSelection(0);
                    return;
                }
                if (!CheckUserInfoUtil.checkPasswordFormat(password)){
                    Toast.makeText(RegisterActivity.this, "您输入的密码长度不在6-20位之间，请重新输入！", Toast.LENGTH_SHORT).show();
                    mPasswordEditText.setText("");
                    mPasswordEditText.setSelection(0);
                    return;
                }
                if (!checkedPassword.equals(password)){
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                    mCheckedPasswordEditText.setText("");
                    mCheckedPasswordEditText.setSelection(0);
                    return;
                }
                userRegister(userPhone,password);
            }
        });
    }

    private void userRegister(final String phone,String password) {
        Company company = mAllCompanies.get(mSpinner.getSelectedItemPosition());
        String companyId = company.getOrgCode();

        final String json = (new Gson()).toJson(company);
        RequestBody body = new FormBody.Builder()
                .add("username",phone)
                .add("password",password)
                .add("org_code",companyId)
                .add("intent","register")
                .build();
        DialogUtil.showProgressDialog(this,mDialog,"正在连接服务器");
        HttpUtil.sendPostRequest(LoginActivity.ADDRESS_LOGIN, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "连接失败：请检查网络连接。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                if (responseText.equals("true")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MoreUserInfoActivity.class);
                            intent.putExtra(LoginActivity.PHONE,phone);
                            intent.putExtra("company_json",json);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, responseText, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

}
