package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.db.inspect.PicsDbSchema;
import com.nisoft.inspectortools.gson.RecodeDataPackage;
import com.nisoft.inspectortools.service.FileUploadService;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeFragment;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.FileUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingFragment extends Fragment {
    private TextView mDatePickerButton;
    private TextView mJobNumberTextView;
    private TextView mDescriptionText;
    private TextView mInspectorTextView;
    private RecyclerView mPicsView;
    private ProgressDialog mDialog;

    private static MaterialInspectRecode sRecodePics;

    private JobPicsAdapter mAdapter;
    private StaggeredGridLayoutManager mManager;

    private boolean isNewJob;
    private String jobType;
    private String mJobNum;
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/工作相册/";
    private String mFolderPath;

    public static WorkingFragment newInstance(String jobNum, String inspectType, boolean isNew) {
        WorkingFragment fragment = new WorkingFragment();
        Bundle args = new Bundle();
        if (jobNum != null) {
            args.putString("job_num", jobNum);
        }
        args.putString(ChooseRecodeTypeFragment.INSPECT_TYPE, inspectType);
        args.putBoolean("isNewJob", isNew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        isNewJob = getArguments().getBoolean("isNewJob");
        mJobNum = getArguments().getString("job_num");
        jobType = getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE);
        mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mFolderPath = PATH + jobType + "/" + mJobNum + "/";
        File file = new File(mFolderPath);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.working_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mJobNum);
        setHasOptionsMenu(true);
        mPicsView = (RecyclerView) view.findViewById(R.id.pics_list);
        mJobNumberTextView = (TextView) view.findViewById(R.id.tv_job_num);
        mDatePickerButton = (TextView) view.findViewById(R.id.date_picker_button);
        mDescriptionText = (TextView) view.findViewById(R.id.job_description);
        mInspectorTextView = (TextView) view.findViewById(R.id.tv_inspector);
        mDialog = new ProgressDialog(getActivity());
        if (!isNewJob) {
            downloadRecode();
        } else {
            sRecodePics = new MaterialInspectRecode();
            sRecodePics.setJobNum(mJobNum);
            sRecodePics.setType(jobType);
            sRecodePics.setPicFolderPath(mFolderPath);
            Date date = new Date();
            sRecodePics.setDate(date);
            sRecodePics.setLatestUpdateTime(date.getTime());
            sRecodePics.setInspectorId(UserLab.getUserLab(getActivity()).getEmployee().getPhone());
            mDatePickerButton.setText(StringFormatUtil.dateFormat(date));
            mInspectorTextView.setText(UserLab.getUserLab(getActivity()).getEmployee().getName());
            mAdapter = new JobPicsAdapter(WorkingFragment.this, R.layout.inspect_image_item, mFolderPath);
            mPicsView.setLayoutManager(mManager);
            mPicsView.setAdapter(mAdapter);
        }
        mJobNumberTextView.setText(mJobNum);
        if (!isNewJob) {
            mJobNumberTextView.setClickable(false);
        } else {
            mJobNumberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditTextActivity.class);
                    intent.putExtra("initText", mJobNumberTextView.getText());
                    startActivityForResult(intent, 3);
                }
            });
        }

        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(0, sRecodePics.getDate());
            }
        });
        mDescriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditTextActivity.class);
                intent.putExtra("initText", mDescriptionText.getText());
                startActivityForResult(intent, 2);
            }
        });
        return view;
    }

    private boolean isExitJobNum(String s) {
        boolean exit = checkJobType(s);
        if (exit) {
            exit = PicsLab.getPicsLab(getActivity()).queryPics(PicsDbSchema.PicTable.Cols.JOB_ID + "=?", new String[]{s}).getCount() > 0;
        } else {
            Toast.makeText(getActivity(), "编号错误！", Toast.LENGTH_SHORT).show();
            exit = true;
        }
        return exit;
    }

    private void onJobNumChanged(final String newJobNum, String oldJobNum) {
        if (isNewJob && !newJobNum.equals(oldJobNum)) {
            PicsLab.getPicsLab(getActivity()).changeJobId(newJobNum, oldJobNum);
            Log.e("local data:", "本地数据更新完成！");
            RequestBody body = new FormBody.Builder()
                    .add("intent", "change_id")
                    .add("newId", newJobNum)
                    .add("oldId", oldJobNum)
                    .build();
            DialogUtil.showProgressDialog(getActivity(), mDialog, "正在更新服务器数据...");
            HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE
                    , body, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDialog.dismiss();
                                    Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String result = response.body().string();
                            Log.e("更新结果：", "onResponse: " + result);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.equals("OK")) {
                                        sRecodePics.setJobNum(newJobNum);
                                        sRecodePics.setLatestUpdateTime(new Date().getTime());
                                        mJobNumberTextView.setText(newJobNum);
                                    }
                                    mDialog.dismiss();
                                }
                            });

                        }
                    });
        }
    }

    private boolean checkJobType(String jobNum) {
        String[] strs = jobNum.split("-");
        if (strs.length > 1) {
            if (strs.length == 3) {
                if ("外购件".equals(jobType)) {
                    return true;
                }
            } else if (strs.length == 2) {
                if (strs[1].startsWith("0")) {
                    if ("非金属材料".equals(jobType)) {
                        return true;
                    }
                } else if (strs[1].startsWith("5")) {
                    if ("金属材料".equals(jobType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     * 用于检查更改后的编号是否重号，不重号则更改服务器和本地记录
     * @param s 需要判断是否存在于服务器上的记录编号
     */
    private void isExitOnServer(final String s) {
        RequestBody body = new FormBody.Builder()
                .add("intent", "jub_num")
                .add("job_id", s)
                .build();
        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在加载记录...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE
                , body, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        final boolean exit = Boolean.parseBoolean(result);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                if (exit) {
                                    Toast.makeText(getActivity(), "您输入的编号已被使用！", Toast.LENGTH_SHORT).show();
                                } else {
                                    onJobNumChanged(s, sRecodePics.getJobNum());
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recode_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.data_push:
                //将实体 格式化为字符串
                String data = sRecodePics.toString();
                //在分线程写入字符串到指定目录的文件下
                File file = new File(mFolderPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileUtil.writeStringToFile(data, mFolderPath + sRecodePics.getJobNum() + ".txt");
                Toast.makeText(getActivity(), "导出数据完成！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.data_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, sRecodePics.toString());
                startActivity(i);
                break;
            case R.id.data_upload:
                uploadJob();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics, sRecodePics.getJobNum());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0:
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                sRecodePics.setDate(date);
                sRecodePics.setLatestUpdateTime(new Date().getTime());
                mDatePickerButton.setText(StringFormatUtil.dateFormat(sRecodePics.getDate()));
                break;
            case 1:
                final String path = data.getStringExtra("PhotoPath");
                final String resourcePhotoPath = data.getStringExtra("resourcePhotoPath");
                final int position = data.getIntExtra("position", -1);
                sRecodePics.setLatestUpdateTime(new Date().getTime());
                if (path != null && position > -1) {
                    mAdapter.resetPath();
                    if (resourcePhotoPath != null) {
                        Snackbar.make(mPicsView, "已移动照片至" + path + ",点击撤销", Snackbar.LENGTH_LONG)
                                .setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FileUtil.moveFile(path, resourcePhotoPath);
                                        mAdapter.resetPath();
                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                                .show();
                    }
                } else if (path == null) {
                    Toast.makeText(getActivity(), "照片已存在，请重新选择！", Toast.LENGTH_LONG).show();
                }
                mAdapter.resetPath();
                break;
            case 2:
                String text = data.getStringExtra("content_edit");
                sRecodePics.setDescription(text);
                sRecodePics.setLatestUpdateTime(new Date().getTime());
                mDescriptionText.setText(text);
                break;
            case 3:
                String jobNum = data.getStringExtra("content_edit");
                Log.e("newJobNum:", jobNum);
                if (isExitJobNum(jobNum)) {
                    Toast.makeText(getActivity(), "您输入的编号已存在！", Toast.LENGTH_SHORT).show();
                } else {
                    isExitOnServer(jobNum);
                }


        }
    }

    public static MaterialInspectRecode getsRecodePics() {
        if (sRecodePics == null) {
            sRecodePics = new MaterialInspectRecode();
        }
        return sRecodePics;
    }


    private void showDatePickerDialog(int requestCode, Date date) {
        FragmentManager fm = getFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(-1, date);
        dialog.setTargetFragment(WorkingFragment.this, requestCode);
        dialog.show(fm, "date");
    }

    public TextView getJobNumberTextView() {
        return mJobNumberTextView;
    }

    public void removeSelectedPic(int position) {
        File file = new File(mAdapter.getPath().get(position));
        file.delete();
        mAdapter.refreshPath();
        mAdapter.notifyDataSetChanged();
    }

    private void downloadRecode() {
        MaterialInspectRecode localRecode = PicsLab.getPicsLab(getActivity()).getPicsByJobNum(mJobNum);
        Log.e("localRecode", localRecode.getLatestUpdateTime() + "");
        localRecode.setPicFolderPath(mFolderPath);
        downloadRecodeFromServer(localRecode);
    }

    /***
     * 从服务器下载记录，和本地记录的时间戳比较，设置记录为最新记录
     * @param localRecode 本地记录
     */
    private void downloadRecodeFromServer(final MaterialInspectRecode localRecode) {
        RequestBody body = new FormBody.Builder()
                .add("intent", "recoding")
                .add("job_id", mJobNum)
                .add("type", jobType)
                .build();
        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在加载记录...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE
                , body, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.e("workingFragment:", result);
                        Gson gson = new Gson();
                        RecodeDataPackage dataPackage = gson.fromJson(result, RecodeDataPackage.class);
                        MaterialInspectRecode serviceRecode = dataPackage.getRecode();
                        final String inspector = dataPackage.getName();
                        sRecodePics = findLaterRecode(localRecode,serviceRecode);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "记录加载完成！", Toast.LENGTH_SHORT).show();
                                mAdapter = new JobPicsAdapter(WorkingFragment.this,
                                        R.layout.inspect_image_item, mFolderPath);
                                mAdapter.setRecode(sRecodePics);
                                mAdapter.setEditable(true);
                                if (!sRecodePics.getInspectorId()
                                        .equals(UserLab.getUserLab(getActivity()).getEmployee().getPhone())) {
                                    setJobUnEdit();
                                }
                                mInspectorTextView.setText(inspector);
                                mPicsView.setLayoutManager(mManager);
                                mPicsView.setAdapter(mAdapter);
                                refreshView();
                            }
                        });
                    }
                });
    }

    /***
     * 同步记录，上传本地照片到服务器，下载服务器上的照片
     */
    private void uploadJob() {
        PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics, sRecodePics.getJobNum());
        Gson gson = new Gson();
        String jobJson = gson.toJson(sRecodePics);
        RequestBody body = new FormBody.Builder()
                .add("intent", "upload")
                .add("job_json", jobJson)
                .build();

        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在上传数据...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_MATERIAL_RECODE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "获取网络连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        if (result.equals("OK")) {
                            Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), FileUploadService.class);
                            intent.putExtra("folder_path", sRecodePics.getPicFolderPath());
                            getActivity().startService(intent);
                        } else {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    /***
     * 找出最新记录
     * @param recode1 记录1
     * @param recode2 记录2
     * @return laterRecode
     */
    private MaterialInspectRecode findLaterRecode(MaterialInspectRecode recode1
            ,MaterialInspectRecode recode2){
        if(recode1.getLatestUpdateTime()>recode2.getLatestUpdateTime()){
            return recode1;
        }
        return recode2;
    }

    /***
     * 刷新界面
     */
    private void refreshView() {
        if (sRecodePics.getDescription() != null) {
            mDescriptionText.setText(sRecodePics.getDescription());
        }
        Date date;
        if (sRecodePics.getDate() != null) {
            date = sRecodePics.getDate();
        } else {
            date = new Date();
            sRecodePics.setDate(date);
        }
        mDatePickerButton.setText(StringFormatUtil.dateFormat(date));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
        Intent intent = new Intent(getActivity(), FileUploadService.class);
        getActivity().stopService(intent);
    }

    /***
     * 设置记录不可编辑
     */
    private void setJobUnEdit() {
        mInspectorTextView.setClickable(false);
        mJobNumberTextView.setClickable(false);
        mDatePickerButton.setClickable(false);
        mDescriptionText.setClickable(false);
        mAdapter.setEditable(false);
    }

    private void synchronizeImages(){
        ArrayList<String> urls = mAdapter.getPath();
        for (String url:urls){
            if(url.startsWith("http")){
                downloadImage(url);
            }else{
                uploadImage(url);
            }
        }
    }

    private void uploadImage(String url) {

    }

    private void downloadImage(String url) {
    }
}

