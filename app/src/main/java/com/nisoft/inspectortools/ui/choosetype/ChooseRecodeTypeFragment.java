package com.nisoft.inspectortools.ui.choosetype;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.org.PositionInfo;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.gson.EmployeeListPackage;
import com.nisoft.inspectortools.ui.login.MoreUserInfoActivity;
import com.nisoft.inspectortools.ui.settings.ContactsActivity;
import com.nisoft.inspectortools.ui.typeinspect.JobListActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemListActivity;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.KEY_SELECTED_TYPE;
import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.RECODE_TYPE_CHI;

/**
 * Created by Administrator on 2017/5/29.
 */

public class ChooseRecodeTypeFragment extends Fragment {
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_job_type, container, false);
        setHasOptionsMenu(true);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) view.findViewById(R.id.nav_view);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        mNavView.setCheckedItem(R.id.update_info);
        mNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Employee employee = UserLab.getUserLab(getActivity()).getEmployee();
                        String phone = employee.getPhone();
                        String companyId = employee.getCompanyId();
                        switch (item.getItemId()) {
//                            case R.id.self_recode:
//                                break;
                            case R.id.update_info:
                                downLoadDataFromServer(companyId,phone);
                                break;
                            case R.id.contacts:
                                Intent intent = new Intent(getActivity(), ContactsActivity.class);
                                intent.putExtra("company_id", UserLab.getUserLab(getActivity()).getEmployee().getCompanyId());
                                startActivity(intent);
                                break;
                            case R.id.self_info_settings:
                                Intent intent1 = new Intent(getActivity(), MoreUserInfoActivity.class);
                                OrgInfo company = OrgLab.getOrgLab(getActivity()).findOrgInfoById(companyId);
                                String companyName = company.getOrgName();
                                intent1.putExtra("company_name", companyName);
                                intent1.putExtra("company_id", companyId);
                                intent1.putExtra("phone", phone);
                                intent1.putExtra("call_mode","setting");
                                startActivity(intent1);
                                break;
                            case R.id.member_info_settings:
                                Toast.makeText(getActivity(), "没有权限！", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.company_info_settings:
                                Toast.makeText(getActivity(), "没有权限！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        mListView = (ListView) view.findViewById(R.id.recode_type_list);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, RECODE_TYPE_CHI);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position < 3) {
                    intent = new Intent(getActivity(), JobListActivity.class);
                    intent.putExtra(KEY_SELECTED_TYPE, position);
                    startActivity(intent);
                } else if (position == 3) {
                    intent = new Intent(getActivity(), ProblemListActivity.class);
                    intent.putExtra(KEY_SELECTED_TYPE, position);
                    startActivity(intent);
                }

            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 11:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getActivity(), "未获得权限，应用无法正常使用", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void downLoadDataFromServer(String companyId,final String phone){
        RequestBody body = new FormBody.Builder()
                .add("company_id", companyId)
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
                OrgLab orgLab = OrgLab.getOrgLab(getActivity());
                ArrayList<PositionInfo> positionList = listPackage.getPositionList();
                orgLab.updateEmployee(employees);
                orgLab.updateOrgs(orgInfoList);
                orgLab.updatePositions(positionList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(OrgLab.getOrgLab(getActivity()).findEmployeeById(phone)==null){
                            Toast.makeText(getActivity(), "请联系管理员加入公司！", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "更新数据成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
