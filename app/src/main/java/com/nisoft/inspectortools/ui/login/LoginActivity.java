package com.nisoft.inspectortools.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;
import com.nisoft.inspectortools.utils.HttpUtil;

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
    public static final String ADDRESS_LOGIN = "http://192.168.31.189:8080/InspectorTools/LoginServlet";
    private EditText mPhoneEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //从SharePreference中获取用户名和密码，连接服务器验证，成功自动登陆，失败将读取的用户名密码设置在输入框中
        sp = getSharedPreferences("user", MODE_PRIVATE);
        editor = sp.edit();
        String remPhone = sp.getString("phone", "");
        String remPassword = sp.getString("password", "");
        if (!remPhone.equals("")&&!remPassword.equals("")){
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
        HttpUtil.sendOkHttpRequest(ADDRESS_LOGIN, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登陆失败：请检查网络连接。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.e("TAG",responseText);
                final boolean canLogin = Boolean.parseBoolean(responseText);
                if (canLogin) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editor.putString("phone", phone);
                            editor.putString("password", password);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, ChooseRecodeTypeActivity.class);
                            intent.putExtra(PHONE, phone);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "登陆失败：用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
