package com.nisoft.inspectortools.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.gson.EmployeeListPackage;
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
        String org_id = intent.getStringExtra("org_id");
        downloadData();
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadData() {
        RequestBody body = new FormBody.Builder()
//                .add("org_id",org_id)
                .add("intent","employees")
                .build();
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MEMBER_INFO, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("UpdateDataService",result);
                Gson gson = new Gson();
                EmployeeListPackage listPackage = gson.fromJson(result,EmployeeListPackage.class);
                ArrayList<Employee> employees = listPackage.getEmployees();
                OrgLab.getOrgLab(UpdateDataService.this).updateEmployee(employees);
            }
        });
    }
}
