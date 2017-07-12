package com.nisoft.inspectortools.ui.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.org.Position;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/12.
 */

public class ContactsFragment extends Fragment {
    private ListView mContactListView;
    private ListView mOrgListView;
    private ArrayList<Employee> mEmployeeList;
    private ArrayList<OrgInfo> mOrgList;
    private ContactAdapter mContactAdapter;
    private OrgAdapter mOrgAdapter;

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
        resetData(parentId);
        mContactAdapter = new ContactAdapter();
        mOrgAdapter = new OrgAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        mContactListView = (ListView) view.findViewById(R.id.lv_contact);
        mOrgListView = (ListView) view.findViewById(R.id.lv_org);
        mContactListView.setAdapter(mContactAdapter);
        mOrgListView.setAdapter(mOrgAdapter);
        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mOrgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetData(mOrgList.get(position).getOrgId());
                mContactAdapter.notifyDataSetChanged();
                mOrgAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void resetData(String parentId){
        mEmployeeList = OrgLab.getOrgLab(getActivity()).getEmployeesByOrg(parentId);
        mOrgList = OrgLab.getOrgLab(getActivity()).findOrgsByParent(parentId);
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
                convertView = View.inflate(getActivity(),android.R.layout.simple_list_item_1,null);
            }
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            Employee employee = mEmployeeList.get(position);
            String orgId = employee.getOrgId();
            OrgInfo org = OrgLab.getOrgLab(getActivity()).findOrgInfoById(orgId);
            String positionId = employee.getPostionId();
            Position position1 = OrgLab.getOrgLab(getActivity()).findPositionById(positionId);
            textView.setText(employee.getName()+"   "+org.getOrgName()+"    "+position1.getPositionName());
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
