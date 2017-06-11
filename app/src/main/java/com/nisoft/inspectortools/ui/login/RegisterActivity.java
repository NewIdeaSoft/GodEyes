package com.nisoft.inspectortools.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.EmployeeInfo;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;
import com.nisoft.inspectortools.utils.CheckUserInfoUtil;
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

    private String initPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        initView();
    }

    private void init() {
        initPhone = getIntent().getStringExtra(LoginActivity.PHONE);
    }

    private void initView() {
        mPhoneEditText = (EditText) findViewById(R.id.et_phone);
        if (initPhone!=null){
            mPhoneEditText.setText(initPhone);
        }
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mCheckedPasswordEditText = (EditText) findViewById(R.id.et_checked_password);
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
                    Toast.makeText(RegisterActivity.this, "您输入的密码长度不在6-16位之间，请重新输入！", Toast.LENGTH_SHORT).show();
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
        RequestBody body = new FormBody.Builder()
                .add("username",phone)
                .add("password",password)
                .add("register","true")
                .build();
        HttpUtil.sendOkHttpRequest(LoginActivity.ADDRESS_LOGIN, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "连接失败：请检查网络连接。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean success = Boolean.parseBoolean(responseText);
                if (success){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RegisterActivity.this, ChooseRecodeTypeActivity.class);
                            intent.putExtra(LoginActivity.PHONE,phone);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "用户已存在，请确认手机号！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }




    /***
     * 从服务器获取下级单位清单
     * @param parentOrgCode 上级组织代码
     * @return 下级单位清单
     */
    private ArrayList<OrgInfo> getOrgInfoFromServer(@Nullable String parentOrgCode){
        ArrayList<OrgInfo> orgsInfo = new ArrayList<>();
        return orgsInfo;
    }

}
