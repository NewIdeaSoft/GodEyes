package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nisoft.inspectortools.R;

/**
 * Created by Administrator on 2017/6/1.
 */

public class EditModeDialog extends DialogFragment {
    private Button mEditButton;
    private Button mChooseButton;
    public static EditModeDialog newInstance(int position,String author){
        Bundle args = new Bundle();
        args.putInt("Content position",position);
        args.putString("init_author",author);
        EditModeDialog fragment = new EditModeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_choose_edit_mode,container,false);
        mEditButton = (Button) view.findViewById(R.id.edit_author);
        mChooseButton = (Button) view.findViewById(R.id.choose_author);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditTextActivity.class);
                intent.putExtra("initText",getArguments().getString("init_author"));
                startActivityForResult(intent,0);
            }
        });
        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode",requestCode+"");
        if (resultCode!=Activity.RESULT_OK){
            return;
        }

        String author = null;
        switch (requestCode){
            case 0:
                author = data.getStringExtra("content_edit");
                break;
            case 1:
                Uri contactUri = data.getData();
                String[] queryFields = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                Cursor cursor =getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
                if(cursor.getCount()==0) {
                    cursor.close();
                    return;
                }
                cursor.moveToFirst();
                author = cursor.getString(0);
                cursor.close();
                break;
            default:
                break;
        }
        Intent intent =  new Intent();
        int position = getArguments().getInt("Content position");
        Log.e("position",position+"");
        intent.putExtra("Content position",position);
        intent.putExtra("AuthorName",author);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        this.dismiss();
    }

}
