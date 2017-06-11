package com.nisoft.inspectortools.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.EmployeeInfo;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;

import java.util.ArrayList;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {

    private Button mDoneButton;
    private ListView mOrgItemListView;
    private ListView mPositionListView;
    private OrgItemAdapter mOrgItemAdapter;

    private EmployeeInfo mEmplyeeInfo;
    private ArrayList<OrgInfo> mOrgInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
        initView();
    }

    private void init() {
        mEmplyeeInfo = new EmployeeInfo();
        mOrgInfos = new ArrayList<>();
        OrgInfo orgInfo = new OrgInfo();
        mOrgInfos.add(orgInfo);
    }

    private void initView() {
        mDoneButton = (Button) findViewById(R.id.btn_register_done);
        mOrgItemListView = (ListView) findViewById(R.id.lv_org_item);
        mOrgItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == mOrgInfos.size()) {
                    ArrayList<String> mOrgList;
                    mOrgInfos.add(new OrgInfo());
                    if(position == 0) {
                        mOrgList = getOrgNames(getOrgInfoFromServer(null));
                    }else{
                        String parentCode = mOrgInfos.get(position-2).getOrgId();
                        mOrgList = getOrgNames(getOrgInfoFromServer(parentCode));
                    }
                    
                }
            }
        });
        mPositionListView = (ListView) findViewById(R.id.lv_position);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userRegister()){
                    Intent intent = new Intent(RegisterActivity.this, ChooseRecodeTypeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean userRegister() {
        return true;
    }

    class OrgItemAdapter extends BaseAdapter{
        ArrayList<String> strings;
        OrgItemAdapter(ArrayList<String> strs){
            strings = strs;
        }

        @Override
        public int getCount() {
            return mOrgInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrgInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(RegisterActivity.this,R.layout.listi_tem_org_item,null);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_org_item);
            Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner_org_item);
            //adapter；data
//            spinner.setSelection();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    mOrgInfos.set(position,)
//                    if (mOrgInfos.size()<组织机构层级)
//                    mOrgInfos.add(new OrgInfo());
//                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return convertView;
        }
    }

    /***
     * 从服务器获取下级单位清单
     * @param parentOrgCode 上级组织代码
     * @return 下级单位清单
     */
    private ArrayList<OrgInfo> getOrgInfoFromServer(@Nullable String parentOrgCode){
        ArrayList<OrgInfo> orgsInfo = new ArrayList<>();
        return orgsInfo;
    }

    private ArrayList<String> getOrgNames(ArrayList<OrgInfo> info){
        ArrayList<String> nameList = new ArrayList<>();
        for (OrgInfo orgInfo : mOrgInfos) {
            nameList.add(orgInfo.getOrgName());
        }
        return nameList;
    }
}
