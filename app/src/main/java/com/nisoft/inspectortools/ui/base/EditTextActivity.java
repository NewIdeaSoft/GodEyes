package com.nisoft.inspectortools.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;

import com.nisoft.inspectortools.R;

public class EditTextActivity extends AppCompatActivity {
    private EditText mAuthorEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        mAuthorEdit = (EditText) findViewById(R.id.edit_text_author);
        String initText = getIntent().getStringExtra("initText");
        mAuthorEdit.setText(initText);
        if(initText!=null) {
            mAuthorEdit.setSelection(initText.length());
        }else {
            mAuthorEdit.setSelection(0);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String contentEdit = mAuthorEdit.getText().toString();
            if (contentEdit != null) {
                Intent intent = new Intent();
                intent.putExtra("content_edit", contentEdit);
                int content_position = getIntent().getIntExtra("content_position",-1);
                if (content_position>-1){
                    intent.putExtra("content_position",content_position);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                setResult(Activity.RESULT_CANCELED);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
