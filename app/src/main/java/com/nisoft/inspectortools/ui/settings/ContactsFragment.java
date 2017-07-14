package com.nisoft.inspectortools.ui.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.org.PositionInfo;
import com.nisoft.inspectortools.ui.base.ChooseMemberDialog;
import com.nisoft.inspectortools.ui.typeinspect.WorkingActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemRecodeActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ContactsFragment extends Fragment {
    private ListView mContactListView;
    private ListView mOrgListView;
    private Button mBackButton;
    private TextView mTitleTextView;
    private TextView mContactTitleTextView;
    private TextView mOrgTitleTextView;
    private ArrayList<Employee> mEmployeeList;
    private ArrayList<OrgInfo> mOrgList;
    private ContactAdapter mContactAdapter;
    private OrgAdapter mOrgAdapter;
    private String mParentId;

    public static ContactsFragment newInstance(String parentId) {
        Log.e("ContactsFragment",parentId);
        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        args.putString("parent_id",parentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentId = getArguments().getString("parent_id");
        mContactAdapter = new ContactAdapter();
        mOrgAdapter = new OrgAdapter();
        mEmployeeList = OrgLab.getOrgLab(getActivity()).getEmployeesByOrg(mParentId);
        if (mEmployeeList==null){
            mEmployeeList = new ArrayList<>();
        }
        mOrgList = OrgLab.getOrgLab(getActivity()).findOrgsByParent(mParentId);
        if (mOrgList==null){
            mOrgList = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        mContactListView = (ListView) view.findViewById(R.id.lv_contact);
        mOrgListView = (ListView) view.findViewById(R.id.lv_org);
        mBackButton = (Button) view.findViewById(R.id.back_to_last);
        mTitleTextView = (TextView) view.findViewById(R.id.org_title);
        mContactTitleTextView = (TextView) view.findViewById(R.id.tv_contacts_title);
        mOrgTitleTextView = (TextView) view.findViewById(R.id.tv_org_title);
        OrgInfo org = OrgLab.getOrgLab(getActivity()).findOrgInfoById(mParentId);
        String orgName = org.getOrgName();
        mTitleTextView.setText(orgName);
        if(mEmployeeList.size()==0) {
            mContactTitleTextView.setVisibility(View.GONE);
        }else {
            mContactTitleTextView.setVisibility(View.VISIBLE);
        }
        if(mOrgList.size()==0) {
            mOrgTitleTextView.setVisibility(View.GONE);
        }else {
            mOrgTitleTextView.setVisibility(View.VISIBLE);
        }
        mContactListView.setAdapter(mContactAdapter);
        mOrgListView.setAdapter(mOrgAdapter);
        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee employee = mEmployeeList.get(position);
                if (getActivity() instanceof ProblemRecodeActivity||getActivity() instanceof WorkingActivity){
                    Intent intent = new Intent();
                    intent.putExtra("author_name", employee.getName());
                    intent.putExtra("author_id", employee.getPhone());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    ((ChooseMemberDialog)getParentFragment()).dismiss();
                }else if(getActivity() instanceof ContactsActivity){

                }
            }
        });
        mOrgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mParentId = mOrgList.get(position).getOrgId();
                refreshView();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrgInfo orgInfo = OrgLab.getOrgLab(getActivity()).findOrgInfoById(mParentId);
                if (orgInfo==null)return;
                mParentId = orgInfo.getParentOrgId();
                if (mParentId.equals("NONE")){
                    if (getActivity() instanceof ContactsActivity) {
                        getActivity().finish();
                    }
                }else {
                    refreshView();
                }
            }
        });
        return view;
    }
    private void refreshView(){
        if (mParentId.equals("NONE")){
            return;
        }
        OrgInfo org = OrgLab.getOrgLab(getActivity()).findOrgInfoById(mParentId);
        String orgName = org.getOrgName();
        mEmployeeList = OrgLab.getOrgLab(getActivity()).getEmployeesByOrg(mParentId);
        if (mEmployeeList==null){
            mEmployeeList = new ArrayList<>();
        }
        mOrgList = OrgLab.getOrgLab(getActivity()).findOrgsByParent(mParentId);
        if (mOrgList==null){
            mOrgList = new ArrayList<>();
        }
        mTitleTextView.setText(orgName);
        if(mEmployeeList.size()==0) {
            mContactTitleTextView.setVisibility(View.GONE);
        }else {
            mContactTitleTextView.setVisibility(View.VISIBLE);
        }
        if(mOrgList.size()==0) {
            mOrgTitleTextView.setVisibility(View.GONE);
        }else {
            mOrgTitleTextView.setVisibility(View.VISIBLE);
        }
        mContactAdapter.notifyDataSetChanged();
        mOrgAdapter.notifyDataSetChanged();
    }
    class ContactAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mEmployeeList.size();
        }

        @Override
        public Object getItem(int position) {
            return mEmployeeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null) {
                convertView = View.inflate(getActivity(),R.layout.contacts_item,null);
            }
            TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_contact_name);
            TextView orgTextView = (TextView) convertView.findViewById(R.id.tv_contact_phone);
            TextView positionTextView = (TextView) convertView.findViewById(R.id.tv_contact_position);
            Employee employee = mEmployeeList.get(position);
            nameTextView.setText(employee.getName());
            String orgId = employee.getOrgId();
            OrgInfo org = OrgLab.getOrgLab(getActivity()).findOrgInfoById(orgId);
            orgTextView.setText(org.getOrgName());
            String positionId = employee.getPositionId();
            Log.e("ContactsFragment",positionId);
            PositionInfo position1 = OrgLab.getOrgLab(getActivity()).findPositionById(positionId);
            positionTextView.setText(position1.getPositionName());
            return convertView;
        }
    }
    class OrgAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mOrgList.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = View.inflate(getActivity(),android.R.layout.simple_list_item_1,null);
            }
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(mOrgList.get(position).getOrgName());
            return convertView;
        }
    }
}
