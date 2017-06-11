package com.nisoft.inspectortools.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity {
    private static final String PHONE = "phone";
    private EditText mPhoneEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //从SharePreference中获取用户名和密码，连接服务器验证，成功自动登陆，失败将读取的用户名密码设置在输入框中
        mPhoneEditText = (EditText) findViewById(R.id.edit_phone);
        mPassWordEditText = (EditText) findViewById(R.id.edit_password);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mRegisterButton = (Button) findViewById(R.id.btn_register);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneEditText.getText().toString();
                String password = mPassWordEditText.getText().toString();
                if(!checkPhoneFormat(phone)) {
                    Toast.makeText(LoginActivity.this, "您输入的不是手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!checkPasswordFormat(password)) {
                    return;
                }
                if(checkUser(phone,password)) {
                    Intent intent = new Intent(LoginActivity.this, ChooseRecodeTypeActivity.class);
                    intent.putExtra(PHONE,mPhoneEditText.getText());
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                String phone = mPhoneEditText.getText().toString();
                if (checkPhoneFormat(phone)){
                    intent.putExtra(PHONE,phone);
                }
                startActivity(intent);
            }
        });
    }

    /***
     * 连接服务器，验证用户名密码
     * @param phone 手机号
     * @param password 密码
     * @return true 验证成功 false 验证失败
     */
    private boolean checkUser(String phone,String password){
        return true;
    }
    private boolean checkPhoneFormat(String phone){
        if(phone.length()!=11){
            return false;
        }
        if (phone.startsWith("13")||phone.startsWith("18")
                ||phone.startsWith("14")||phone.startsWith("15")||phone.startsWith("17")){
            return true;
        }
        return false;
    }
    private boolean checkPasswordFormat(String password){
        if (password.length()>=6&&password.length()<=20){
            return true;
        }
        return false;
    }

}
