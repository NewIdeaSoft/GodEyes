package com.nisoft.inspectortools.ui.base;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.ui.settings.ContactsFragment;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class ChooseMemberDialog extends DialogFragment {

    public static ChooseMemberDialog newInstance(String parentId) {
        Bundle args = new Bundle();
        args.putString("parent_id",parentId);
        ChooseMemberDialog fragment = new ChooseMemberDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_contacts, container, false);
        FragmentManager fm = getChildFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.contact_fragment_content);
        if (fragment == null) {
            String parentId = getArguments().getString("parent_id");
            fragment = ContactsFragment.newInstance(parentId);
            fragment.setTargetFragment(getTargetFragment(),getTargetRequestCode());
            fm.beginTransaction().add(R.id.contact_fragment_content, fragment).commit();
        }
//        this.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_DialogWhenLarge);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.95),(int) (dm.heightPixels * 0.80) );
        }
    }
}
