package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.org.PositionInfo;
import com.nisoft.inspectortools.gson.EmployeeListPackage;
import com.nisoft.inspectortools.gson.OrgListPackage;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoreUserInfoActivity extends AppCompatActivity {
    private static final int REQUEST_NAME = 1001;
    private static final int REQUEST_NUM = 1002;
    private TextView mNameTextView;
    private TextView mEmployeeNumTextView;
    private TextView mCompanyNameTextView;
    private TextView mPositionTextView;
    private ListView mOrgListView;
    private ListView mStationListView;
    private Button mDoneButton;
    private ProgressDialog mDialog;

    private String phone;
    private Employee mEmployee;
    private String mCompanyId;
    private String mCompanyName;
    private ArrayList<OrgInfo> mOrgInfo;
    private ArrayList<String> mStationsInfo;
    private OrgInfoAdapter mOrgInfoAdapter;
    private String mCallMode;

    private ArrayList<ArrayList<OrgInfo>> mOrgsForChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_info);
        init();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            return;
        }
        String resultText = data.getStringExtra("edit_content");
        switch (requestCode) {
            case REQUEST_NAME :
                mNameTextView.setText(resultText);
                mEmployee.setName(resultText);
                break;
            case REQUEST_NUM:
                mEmployeeNumTextView.setText(resultText);
                mEmployee.setWorkNum(resultText);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
            uploadMoreInfo();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        phone = getIntent().getStringExtra("phone");
        mCompanyId = getIntent().getStringExtra("company_id");
        mCompanyName = getIntent().getStringExtra("company_name");
        mCallMode = getIntent().getStringExtra("call_mode");
        mOrgsForChoose = new ArrayList<>();
        mOrgInfoAdapter = new OrgInfoAdapter();
    }


    private void initView() {
        mDialog = new ProgressDialog(this);
        mNameTextView = (TextView) findViewById(R.id.tv_name);
        mNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditActivity(mNameTextView.getText().toString(),REQUEST_NAME);
            }
        });
        mEmployeeNumTextView = (TextView) findViewById(R.id.tv_member_num);
        mEmployeeNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditActivity(mEmployeeNumTextView.getText().toString(),REQUEST_NUM);
            }
        });
        mCompanyNameTextView = (TextView) findViewById(R.id.tv_company_name);
        mPositionTextView = (TextView) findViewById(R.id.tv_member_position);
        mOrgListView = (ListView) findViewById(R.id.lv_org_info_item);
        mStationListView = (ListView) findViewById(R.id.lv_position_info);
        mDoneButton = (Button) findViewById(R.id.btn_info_upload);
        if (mCompanyId != null && !mCompanyId.equals("")) {
            mCompanyNameTextView.setText(mCompanyName);
            downLoadDataFromLocal();
        }
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInfo()) {
                    uploadMoreInfo();
                }
            }
        });
        mOrgListView.setAdapter(mOrgInfoAdapter);
    }

    private boolean checkInfo() {
        if (mNameTextView.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
    private void downLoadDataFromLocal(){
        mEmployee = OrgLab.getOrgLab(this).findEmployeeById(phone);
        if (mEmployee == null){
            downLoadDataFromServer();
        }else{
            OrgInfo org = OrgLab.getOrgLab(this).findOrgInfoById(mEmployee.getOrgId());
            mOrgInfo = new ArrayList<>();
            Log.e("downLoadDataFromLocal",org.getOrgName());
            mOrgsForChoose.add(0,OrgLab.getOrgLab(this).findOrgsByParent(org.getParentOrgId()));
            while(!org.getParentOrgId().contains("NONE")){
                mOrgInfo.add(0,org);
                OrgInfo parentOrg = OrgLab.getOrgLab(this).findOrgInfoById(org.getParentOrgId());
                mOrgsForChoose.add(0,OrgLab.getOrgLab(this).findOrgsByParent(org.getParentOrgId()));
                org = parentOrg;
            }
            refreshView();
        }
    }
    private void downLoadDataFromServer(){
        RequestBody body = new FormBody.Builder()
                .add("company_id", mCompanyId)
                .add("intent", "employees")
                .build();
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MEMBER_INFO, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("UpdateDataService", result);
                Gson gson = GsonUtil.getDateFormatGson();
                EmployeeListPackage listPackage = gson.fromJson(result, EmployeeListPackage.class);
                ArrayList<Employee> employees = listPackage.getEmployees();
                ArrayList<OrgInfo> orgInfoList = listPackage.getOrgList();
                OrgLab orgLab = OrgLab.getOrgLab(MoreUserInfoActivity.this);
                ArrayList<PositionInfo> positionList = listPackage.getPositionList();
                orgLab.updateEmployee(employees);
                orgLab.updateOrgs(orgInfoList);
                orgLab.updatePositions(positionList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(OrgLab.getOrgLab(MoreUserInfoActivity.this).findEmployeeById(phone)==null){
                            Toast.makeText(MoreUserInfoActivity.this, "请联系管理员加入公司！", Toast.LENGTH_LONG).show();
                        }else{
                            downLoadDataFromLocal();
                        }
                    }
                });
            }
        });
    }
    private void refreshView(){
        if (mEmployee.getName() != null) {
            mNameTextView.setText(mEmployee.getName());
        }
        if (mEmployee.getWorkNum() != null) {
            mEmployeeNumTextView.setText(mEmployee.getWorkNum());
        }
        if (mEmployee.getPositionId()!=null){
            String positionName = OrgLab.getOrgLab(MoreUserInfoActivity.this)
                    .findPositionById(mEmployee.getPositionId())
                    .getPositionName();
            mPositionTextView.setText(positionName);
        }
        mOrgInfoAdapter.notifyDataSetChanged();
    }
    private void uploadMoreInfo() {
        DialogUtil.showProgressDialog(this, mDialog, "正在上传用户信息...");
        mEmployee.setName(mNameTextView.getText().toString());
        mEmployee.setPhone(phone);
        mEmployee.setWorkNum(mEmployeeNumTextView.getText().toString());
        mEmployee.setCompanyId(mCompanyId);
        for (OrgInfo info : mOrgInfo) {
            if (info != null) {
                mEmployee.setOrgId(info.getOrgId());
            } else {
                break;
            }
        }
        Gson gson = GsonUtil.getDateFormatGson();
        String json = gson.toJson(mEmployee);
        RequestBody body = new FormBody.Builder()
                .add("employee", json)
                .add("intent", "update")
                .build();
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MEMBER_INFO, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(MoreUserInfoActivity.this, "网络链接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                if (s.equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoreUserInfoActivity.this, "用户信息提交成功！", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            OrgLab.getOrgLab(MoreUserInfoActivity.this).updateEmployee(mEmployee);
                            if(mCallMode.equals("login")) {
                                Intent intent = new Intent(MoreUserInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Toast.makeText(MoreUserInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void getSecondaryOrgsFromLocal(String parentId, final int parentLevel){
        ArrayList<OrgInfo> orgList = OrgLab.getOrgLab(this).findOrgsByParent(parentId);
        mOrgsForChoose.set(parentLevel + 1,orgList);
        mOrgInfoAdapter.notifyDataSetChanged();
    }
    private int getSelectedOrgPosition(OrgInfo selectedOrg, ArrayList<OrgInfo> Orgs) {
        for (int i = 0; i < Orgs.size(); i++) {
            OrgInfo org = Orgs.get(i);
            if (org.getOrgId().equals(selectedOrg.getOrgId())) {
                return i;
            }
        }
        return -1;
    }

    private void startEditActivity(String initText,int requestCode){
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("initText",initText);
        startActivityForResult(intent,requestCode);
    }

    class OrgInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mOrgsForChoose.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrgsForChoose.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int itemPosition, View convertView, ViewGroup parent) {
            convertView = View.inflate(MoreUserInfoActivity.this, R.layout.list_tem_org_item, null);
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner_org_item);
            ArrayList<OrgInfo> orgInfos = mOrgsForChoose.get(itemPosition);
            OrgSpinnerAdapter adapter = new OrgSpinnerAdapter(orgInfos);
            spinner.setAdapter(adapter);
            if (mOrgInfo.size() > itemPosition) {
                OrgInfo selectedOrg = mOrgInfo.get(itemPosition);
                if (selectedOrg != null && selectedOrg.getOrgLevel() != 0 && orgInfos != null) {
                    int selected = getSelectedOrgPosition(selectedOrg, orgInfos);
                    Log.e("TAG", "" + selected);
                    if (selected != -1) {
                        spinner.setSelection(selected + 1);
                    }
                }
            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        mOrgInfo.set(itemPosition, mOrgsForChoose.get(itemPosition).get(position - 1));
                        if (itemPosition < mOrgsForChoose.size() - 1) {
                            ArrayList<OrgInfo> secondaryOrgs = mOrgsForChoose.get(itemPosition + 1);
                            OrgInfo parentOrg = mOrgInfo.get(itemPosition);
                            if (secondaryOrgs == null
                                    || (secondaryOrgs.size() != 0
                                    && !secondaryOrgs.get(0).getParentOrgId().equals(parentOrg.getOrgId()))) {
                                Log.e("parent:", mOrgInfo.get(itemPosition).getOrgId());
                                getSecondaryOrgsFromLocal(parentOrg.getOrgId(), itemPosition);
                            }

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return convertView;
        }

    }

    class OrgSpinnerAdapter extends BaseAdapter {
        private ArrayList<OrgInfo> mOrgs;

        public OrgSpinnerAdapter(ArrayList<OrgInfo> orgs) {
            mOrgs = orgs;
        }

        @Override
        public int getCount() {
            if (mOrgs == null) {
                return 1;
            }
            return mOrgs.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            }
            return mOrgs.get(position - 1);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(MoreUserInfoActivity.this, android.R.layout.simple_spinner_item, null);
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            if (position == 0) {
                textView.setText("--请选择--");
                textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                textView.setText(mOrgs.get(position - 1).getOrgName());
            }
            return convertView;
        }
    }
}
