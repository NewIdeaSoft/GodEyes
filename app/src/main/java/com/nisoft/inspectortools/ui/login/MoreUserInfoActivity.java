package com.nisoft.inspectortools.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Company;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.EmployeeDataPackage;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgListPackage;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeActivity;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoreUserInfoActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private EditText mEmployeeNumEditText;
    private TextView mCompanyNameTextView;
    private ListView mOrgListView;
    private ListView mStationListView;
    private Button mDoneButton;
    private ProgressDialog mDialog;

    private String phone;
    private Employee mEmployee;
    private Company mCompany;
    private ArrayList<OrgInfo> mOrgInfo;
    private ArrayList<String> mStationsInfo;
    private int mOrgLevels;
    private OrgInfoAdapter mOrgInfoAdapter;

    private ArrayList<ArrayList<OrgInfo>> mOrgsForChoose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_user_info);
        init();
        initView();
    }

    private void init() {
        phone = getIntent().getStringExtra("phone");
        mOrgLevels = getIntent().getIntExtra("levels", -1);
        Gson gson = new Gson();
        mCompany = gson.fromJson(getIntent().getStringExtra("company_json"), Company.class);
        mOrgsForChoose = new ArrayList<>();
        mOrgInfoAdapter = new OrgInfoAdapter();
    }


    private void initView() {
        mDialog = new ProgressDialog(this);
        mNameEditText = (EditText) findViewById(R.id.et_name);
        mEmployeeNumEditText = (EditText) findViewById(R.id.et_member_num);
        mCompanyNameTextView = (TextView) findViewById(R.id.tv_company_name);
        mOrgListView = (ListView) findViewById(R.id.lv_org_info_item);
        mStationListView = (ListView) findViewById(R.id.lv_position_info);
        mDoneButton = (Button) findViewById(R.id.btn_info_upload);

        mCompanyNameTextView.setText(mCompany.getOrgName());
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMoreInfo();
            }
        });
        mOrgListView.setAdapter(mOrgInfoAdapter);
        downLoadInfo();
    }

    private void downLoadInfo() {

        RequestBody body = new FormBody.Builder()
                .add("phone", phone)
                .add("intent", "query")
                .add("structure_levels", mCompany.getOrgStructure().size() - 1 + "")
                .add("company_id", mCompany.getOrgCode())
                .build();
        String address = HttpUtil.ADRESS_MAIN + HttpUtil.SERVLET_MEMBER_INFO;
        DialogUtil.showProgressDialog(this, mDialog, "正在从服务器加载用户信息...");
        HttpUtil.sendPostRequest(address, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(MoreUserInfoActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("result", result);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("false")) {
                            Toast.makeText(MoreUserInfoActivity.this, "加载用户信息失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Gson gson = new Gson();
                            EmployeeDataPackage dataPackage = gson.fromJson(result, EmployeeDataPackage.class);
                            mEmployee = dataPackage.getEmployee();
                            mOrgInfo = dataPackage.getOrgInfo();
                            mOrgsForChoose = dataPackage.getOrgsInfoForSelect();
                            if (mEmployee.getName() != null) {
                                mNameEditText.setText(mEmployee.getName());
                            }
                            if (mEmployee.getWorkNum() != null) {
                                mEmployeeNumEditText.setText(mEmployee.getWorkNum());
                            }
                            mOrgInfoAdapter.notifyDataSetChanged();
                        }
                        mDialog.dismiss();
                    }
                });

            }
        });
    }

    private void uploadMoreInfo() {
        DialogUtil.showProgressDialog(this, mDialog, "正在上传用户信息...");
        mEmployee.setName(mNameEditText.getText().toString());
        mEmployee.setOrgCode(mOrgInfo.get(mOrgInfo.size()-1).getOrgId());
        mEmployee.setPhone(phone);
        mEmployee.setWorkNum(mEmployeeNumEditText.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(mEmployee);
        RequestBody body = new FormBody.Builder()
                .add("employee", json)
                .add("intent", "update")
                .build();
        String address = HttpUtil.ADRESS_MAIN + HttpUtil.SERVLET_MEMBER_INFO;
        HttpUtil.sendPostRequest(address, body, new Callback() {
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
                if (s.equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Intent intent = new Intent(MoreUserInfoActivity.this, ChooseRecodeTypeActivity.class);
                            startActivity(intent);
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
                if (selectedOrg != null) {
                    int selected = orgInfos.indexOf(selectedOrg);
                    if (selected != -1) {
                        spinner.setSelection(selected);
                    }
                }
            }
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mOrgInfo.set(itemPosition, mOrgsForChoose.get(itemPosition).get(position));

                    if (itemPosition < mOrgsForChoose.size() - 1) {
                        if (mOrgInfo.get(itemPosition + 1) == null) {
                            Log.e("parent:",mOrgInfo.get(itemPosition).getOrgId());
                            getSecondaryOrgs(mOrgInfo.get(itemPosition).getOrgId(), itemPosition);
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

    private void getSecondaryOrgs(String parentId, final int parentLevel) {
        DialogUtil.showProgressDialog(this, mDialog, "正在加载单位列表...");
        RequestBody body = new FormBody.Builder()
                .add("parent_id", parentId)
                .add("intent", "secondary")
                .build();
        String address = HttpUtil.ADRESS_MAIN + HttpUtil.SERVLET_MEMBER_INFO;
        HttpUtil.sendPostRequest(address, body
                , new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(MoreUserInfoActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String result = response.body().string();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("OrgListPackage", result);
                                Gson gson = new Gson();
                                OrgListPackage orgListPackage = gson.fromJson(result, OrgListPackage.class);
                                if (orgListPackage.getOrgInfos().size() > 0) {
                                    mOrgsForChoose.set(parentLevel + 1, orgListPackage.getOrgInfos());
                                    mOrgInfoAdapter.notifyDataSetChanged();
                                }
                                mDialog.dismiss();

                            }
                        });
                    }

                });
    }

    class OrgSpinnerAdapter extends BaseAdapter {
        private ArrayList<OrgInfo> mOrgs;

        public OrgSpinnerAdapter(ArrayList<OrgInfo> orgs) {
            mOrgs = orgs;
        }

        @Override
        public int getCount() {
            if(mOrgs==null) {
                return 0;
            }
            return mOrgs.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(MoreUserInfoActivity.this, android.R.layout.simple_spinner_item, null);
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(mOrgs.get(position).getOrgName());
            return convertView;
        }

        public ArrayList<OrgInfo> getOrgs() {
            return mOrgs;
        }

        public void setOrgs(ArrayList<OrgInfo> orgs) {
            mOrgs = orgs;
        }
    }
}
