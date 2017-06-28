package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Company;
import com.nisoft.inspectortools.bean.org.RegisterDataPackage;
import com.nisoft.inspectortools.bean.org.User;
import com.nisoft.inspectortools.bean.org.UserLab;
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
    private ArrayList<Company> mAllCompanies = new ArrayList<>();
    private CompanySpinnerAdapter mAdapter;

    private SharedPreferences mSpCompany;
    private SharedPreferences.Editor mEditorCompany;
    private SharedPreferences mSpUser;
    private SharedPreferences.Editor mEditorUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        mSpCompany = getSharedPreferences("company", MODE_PRIVATE);
        mEditorCompany = mSpCompany.edit();
        mSpUser = getSharedPreferences("user", MODE_PRIVATE);
        mEditorUser = mSpUser.edit();
        init();
        initView();
    }

    private void init() {
        mDialog = new ProgressDialog(this);
        initPhone = getIntent().getStringExtra(LoginActivity.PHONE);
        getCompaniesFromServer();
        mAdapter = new CompanySpinnerAdapter();
    }

    private void getCompaniesFromServer() {
        DialogUtil.showProgressDialog(this,mDialog,"正在从服务器加载组织信息...");
        HttpUtil.sendGetRequest(HttpUtil.SERVLET_LOGIN, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "连接服务器失败！请检查网络连接。", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("result",result);
                Gson gson = new Gson();
                mAllCompanies = gson.fromJson(result, RegisterDataPackage.class).getCompanies();
                Log.e("mAllCompanies",mAllCompanies.size()+"");
                Log.e("FirstName:",mAllCompanies.get(0).getOrgName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        mAdapter.notifyDataSetChanged();
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

    private void userRegister(final String phone,final String password) {
        final Company company = mAllCompanies.get(mSpinner.getSelectedItemPosition());
        final String companyId = company.getOrgCode();

        final String json = (new Gson()).toJson(company);
        RequestBody body = new FormBody.Builder()
                .add("username",phone)
                .add("password",password)
                .add("org_code",companyId)
                .add("intent","register")
                .build();
        DialogUtil.showProgressDialog(this,mDialog,"正在连接服务器");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_LOGIN, body, new Callback() {
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
                    mEditorCompany.putString("phone",phone);
                    mEditorCompany.putString("company_name",company.getOrgName());
                    mEditorCompany.putString("company_code",company.getOrgCode());
                    mEditorCompany.putString("company_structure",company.getOrgStructure().toString());
                    mEditorCompany.commit();

                    mEditorUser.putString("phone",phone);
                    mEditorUser.putString("password",password);
                    mEditorUser.commit();

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
    class CompanySpinnerAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mAllCompanies.size();
        }

        @Override
        public Object getItem(int position) {
            return mAllCompanies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(RegisterActivity.this,android.R.layout.simple_spinner_item,null);
            TextView textView = (TextView) convertView;
            textView.setText(mAllCompanies.get(position).getOrgName());
            return convertView;
        }
    }
}
