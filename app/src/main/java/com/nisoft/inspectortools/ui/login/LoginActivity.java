package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Company;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity {
    public static final String PHONE = "phone";
    private EditText mPhoneEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private ProgressDialog mDialog;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDialog = new ProgressDialog(this);
        //从SharePreference中获取用户名和密码，连接服务器验证，成功自动登陆，失败将读取的用户名密码设置在输入框中
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editor = sp.edit();
        String remPhone = sp.getString("phone", "");
        String remPassword = sp.getString("password", "");
        if (!remPhone.equals("") && !remPassword.equals("")) {
            checkUser(remPhone, remPassword);
        }
        mPhoneEditText = (EditText) findViewById(R.id.edit_phone);
        mPhoneEditText.setText(remPhone);

        mPassWordEditText = (EditText) findViewById(R.id.edit_password);
        mPassWordEditText.setText(remPassword);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mRegisterButton = (Button) findViewById(R.id.btn_register);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneEditText.getText().toString();
                String password = mPassWordEditText.getText().toString();
                if (!checkPhoneFormat(phone)) {
                    Toast.makeText(LoginActivity.this, "您输入的不是手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkPasswordFormat(password)) {
                    return;
                }
                checkUser(phone, password);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                String phone = mPhoneEditText.getText().toString();
                if (checkPhoneFormat(phone)) {
                    intent.putExtra(PHONE, phone);
                }
                startActivity(intent);
            }
        });
    }

    /***
     * 连接服务器，验证用户名密码
     * @param phone 手机号
     * @param password 密码
     *
     */
    private void checkUser(final String phone, final String password) {
        RequestBody body = new FormBody.Builder()
                .add("username", phone)
                .add("password", password)
                .add("intent", "login")
                .build();
        DialogUtil.showProgressDialog(this, mDialog, "正在登陆...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_LOGIN, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "登陆失败：请检查网络连接。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseText = response.body().string();
                Log.e("TAG", responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (responseText.equals("non_info")) {
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MoreUserInfoActivity.class);
                            Company company = new Company();
                            SharedPreferences spCompany = getSharedPreferences("company", MODE_PRIVATE);
                            if (spCompany.getString("phone", "").equals(phone)) {
                                String companyName = spCompany.getString("company_name", "");
                                String companyId = spCompany.getString("company_code", "");
                                String structure = spCompany.getString("company_structure", "");
                                company.setOrgCode(companyId);
                                company.setOrgName(companyName);
                                company.setOrgStructure(StringFormatUtil.getStrings(structure));
                                Gson gson = GsonUtil.getDateFormatGson();
                                String json = gson.toJson(company);
                                Log.e("LoginActivity", json);
                                intent.putExtra("company_id",companyId);
                                intent.putExtra("company_name", companyName);
                            }

                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            finish();
                        } else if (responseText.equals("error:1")) {
                            Toast.makeText(LoginActivity.this, "登陆失败：密码错误！", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("error:2")) {
                            Toast.makeText(LoginActivity.this, "登陆失败：用户不存在！", Toast.LENGTH_SHORT).show();
                        } else if (responseText.equals("error:3")) {
                            Toast.makeText(LoginActivity.this, "登陆失败：系统错误！", Toast.LENGTH_SHORT).show();
                        } else {
                            Gson gson = GsonUtil.getDateFormatGson();
                            Employee employee = gson.fromJson(responseText, Employee.class);
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                            editor.putString("employee", responseText);
                            editor.commit();
                            UserLab.getUserLab(LoginActivity.this).setEmployee(employee);
                            Intent intent = new Intent(LoginActivity.this, ChooseRecodeTypeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }

    private boolean checkPhoneFormat(String phone) {
        if (phone.length() != 11) {
            return false;
        }
        if (phone.startsWith("13") || phone.startsWith("18")
                || phone.startsWith("14") || phone.startsWith("15")
                || phone.startsWith("17")) {
            return true;
        }
        return false;
    }

    private boolean checkPasswordFormat(String password) {
        if (password.length() >= 6 && password.length() <= 20) {
            return true;
        }
        return false;
    }
}
