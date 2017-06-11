package com.nisoft.inspectortools.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                    return;
                }
                if(!checkPasswordFormat(password)) {
                    return;
                }
                if(checkUser(phone,password)) {
                    Intent intent = new Intent(LoginActivity.this, ChooseRecodeTypeActivity.class);
                    intent.putExtra(PHONE,mPhoneEditText.getText());
                    startActivity(intent);
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean checkUser(String phone,String password){
        return true;
    }
    private boolean checkPhoneFormat(String phone){
        return true;
    }
    private boolean checkPasswordFormat(String password){
        return true;
    }
}
