package com.nisoft.inspectortools.ui.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ContactsFragment extends Fragment {
    private ListView mContactListView;
    private ListView mOrgListView;
    private ArrayList<Employee> mEmployeeList;
    private ArrayList<OrgInfo> mOrgList;

    public static ContactsFragment newInstance(String parentId) {
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        args.putString("parent_id",parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String parentId = getArguments().getString("parent_id");
        mEmployeeList = OrgLab.getOrgLab(getActivity()).getEmployees();
        mOrgList = OrgLab.getOrgLab(getActivity()).findOrgsByParent(parentId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
