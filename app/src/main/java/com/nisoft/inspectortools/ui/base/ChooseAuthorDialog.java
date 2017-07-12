package com.nisoft.inspectortools.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgLab;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/7/9.
 */

public class ChooseAuthorDialog extends DialogFragment {
    private ArrayList<Employee> mEmployees;
    private RecyclerView mContactsRecyclerView;
    private ContactAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_contacts, container, false);
//        this.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_DeviceDefault_DialogWhenLarge);
        mEmployees = OrgLab.getOrgLab(getActivity()).getEmployees();
        Log.e("mEmployees.size()", "" + mEmployees.size());
        mAdapter = new ContactAdapter();
        mContactsRecyclerView = (RecyclerView) view.findViewById(R.id.rv_contacts);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mContactsRecyclerView.setLayoutManager(layoutManager);
        mContactsRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.contacts_item, null);
            ContactViewHolder holder = new ContactViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            final Employee employee = mEmployees.get(position);
            holder.nameTextView.setText(employee.getName());
            holder.phoneTextView.setText(employee.getPhone());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("author_name", employee.getName());
                    intent.putExtra("author_id", employee.getPhone());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    ChooseAuthorDialog.this.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mEmployees.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            TextView nameTextView;
            TextView phoneTextView;
            TextView orgTextView;

            public ContactViewHolder(View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView;
                nameTextView = (TextView) itemView.findViewById(R.id.tv_contact_name);
                phoneTextView = (TextView) itemView.findViewById(R.id.tv_contact_phone);
//                orgTextView = (TextView) itemView.findViewById(R.id.tv_contact_org);
            }
        }
    }
}
