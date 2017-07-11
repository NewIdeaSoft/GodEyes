package com.nisoft.inspectortools.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgInfo;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.gson.EmployeeListPackage;
import com.nisoft.inspectortools.gson.OrgListPackage;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateDataService extends Service {
    public UpdateDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String company_id = intent.getStringExtra("company_id");
        downloadEmployeesData(company_id);
        downloadOrgData(company_id);
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadEmployeesData(String company_id) {
        RequestBody body = new FormBody.Builder()
                .add("company_id", company_id)
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
                OrgLab.getOrgLab(UpdateDataService.this).updateEmployee(employees);
            }
        });
    }

    private void downloadOrgData(String company_id) {
        RequestBody body = new FormBody.Builder()
                .add("company_id", company_id)
                .add("intent", "orgs")
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
                OrgListPackage listPackage = gson.fromJson(result, OrgListPackage.class);
                ArrayList<OrgInfo> orgInfos = listPackage.getOrgInfos();
                OrgLab.getOrgLab(UpdateDataService.this).updateOrgs(orgInfos);
            }
        });
    }
}
