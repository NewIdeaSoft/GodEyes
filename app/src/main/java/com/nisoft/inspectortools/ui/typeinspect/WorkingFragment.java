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
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.db.inspect.PicsDbSchema;
import com.nisoft.inspectortools.gson.RecodeDataPackage;
import com.nisoft.inspectortools.service.FileUploadService;
import com.nisoft.inspectortools.ui.base.ChooseMemberDialog;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.FileUtil;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;
import com.nisoft.inspectortools.utils.UploadData;
import com.nisoft.inspectortools.utils.VolumeImageDownLoad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.KEY_SELECTED_TYPE;
import static com.nisoft.inspectortools.ui.strings.RecodeTypesStrings.RECODE_TYPE_ENG;


/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingFragment extends Fragment {
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/工作相册/";
    private static final int REQUEST_INSPECTOR = 4;
    private static MaterialInspectRecode sRecodePics;
    private TextView mDatePickerButton;
    private TextView mJobNumberTextView;
    private TextView mDescriptionText;
    private TextView mInspectorTextView;
    private RecyclerView mPicsView;
    private ProgressDialog mDialog;
    private JobPicsAdapter mAdapter;
    private StaggeredGridLayoutManager mManager;
    private boolean isNewJob;
    private int mWhichType = -1;
    private String jobType;
    private String mJobNum;
    private String mFolderPath;
    private boolean mEditable = false;
    private boolean isDataChanged = false;

    public static WorkingFragment newInstance(String jobNum, int whichType, boolean isNew) {
        WorkingFragment fragment = new WorkingFragment();
        Bundle args = new Bundle();
        if (jobNum != null) {
            args.putString("job_num", jobNum);
        }
        args.putInt(KEY_SELECTED_TYPE, whichType);
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
        mWhichType = getArguments().getInt(KEY_SELECTED_TYPE);
        isNewJob = getArguments().getBoolean("isNewJob");
        mJobNum = getArguments().getString("job_num");
        jobType = RECODE_TYPE_ENG[mWhichType];
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
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPicsView = (RecyclerView) view.findViewById(R.id.pics_list);
        mJobNumberTextView = (TextView) view.findViewById(R.id.tv_job_num);
        mDatePickerButton = (TextView) view.findViewById(R.id.date_picker_button);
        mDescriptionText = (TextView) view.findViewById(R.id.job_description);
        mInspectorTextView = (TextView) view.findViewById(R.id.tv_inspector);
        mDialog = new ProgressDialog(getActivity());
        if (!isNewJob) {
            downloadRecode();
        } else {
            mEditable = true;
            createNewJob();
            mDatePickerButton.setText(StringFormatUtil.dateFormat(sRecodePics.getDate()));
            mInspectorTextView.setText(UserLab.getUserLab(getActivity()).getEmployee().getName());
            mAdapter = new JobPicsAdapter(WorkingFragment.this, R.layout.inspect_image_item, mFolderPath);
            mAdapter.setEditable(mEditable);
            mPicsView.setLayoutManager(mManager);
            mPicsView.setAdapter(mAdapter);
        }
        mInspectorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsDialog(REQUEST_INSPECTOR);
            }
        });
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
    }

    private void createNewJob() {
        sRecodePics = new MaterialInspectRecode();
        sRecodePics.setJobNum(mJobNum);
        sRecodePics.setType(jobType);
        sRecodePics.setPicFolderPath(mFolderPath);
        Date date = new Date();
        sRecodePics.setDate(date);
        sRecodePics.setLatestUpdateTime(date.getTime());
        sRecodePics.setInspectorId(UserLab.getUserLab(getActivity()).getEmployee().getPhone());
    }

    /***
     *
     * @param s mEditable用户自定义的检验编号
     * @return 本地数据库存在返回true, 不存在返回false
     */
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

    /***
     * 成功更改检验编号时调用，以更新本地和服务器数据库对应的编号
     * @param newJobNum 新编号
     * @param oldJobNum 原编号
     */
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
                                        isDataChanged = true;
                                    }
                                    mDialog.dismiss();
                                }
                            });

                        }
                    });
        }
    }

    /***
     *
     * @param jobNum 检验编号
     * @return 符合规范返回true
     */
    private boolean checkJobType(String jobNum) {
        String[] strs = jobNum.split("-");
        if (strs.length > 1) {
            if (strs.length == 3) {
                if (RECODE_TYPE_ENG[2].equals(jobType)) {
                    return true;
                }
            } else if (strs.length == 2) {
                if (Integer.parseInt(strs[1]) <= 5000) {
                    if (RECODE_TYPE_ENG[1].equals(jobType)) {
                        return true;
                    }
                } else if (Integer.parseInt(strs[1]) > 5000) {
                    if (RECODE_TYPE_ENG[0].equals(jobType)) {
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
                synchronizeRecode();
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
                if (date.equals(sRecodePics.getDate()))return;
                sRecodePics.setDate(date);
                sRecodePics.setLatestUpdateTime(new Date().getTime());
                isDataChanged = true;
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
                isDataChanged = true;
                mDescriptionText.setText(text);
                break;
            case 3:
                String jobNum = data.getStringExtra("content_edit");
                if (isExitJobNum(jobNum)) {
                    Toast.makeText(getActivity(), "您输入的编号已存在！", Toast.LENGTH_SHORT).show();
                } else {
                    isExitOnServer(jobNum);
                }
                break;
            case REQUEST_INSPECTOR:
                String authorId = data.getStringExtra("author_id");
                String authorName = data.getStringExtra("author_name");
                if (!authorId.equals(sRecodePics.getInspectorId())){
                    sRecodePics.setInspectorId(authorId);
                    mInspectorTextView.setText(authorName);
                    sRecodePics.setLatestUpdateTime(new Date().getTime());
                    isDataChanged = true;
                }
                break;

        }
    }

    private void showDatePickerDialog(int requestCode, Date date) {
        FragmentManager fm = getFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(-1, date);
        dialog.setTargetFragment(WorkingFragment.this, requestCode);
        dialog.show(fm, "date");
    }

    /***
     * 删除指定位置的照片
     * @param position
     */
    public void removeSelectedPic(int position) {
        File file = new File(mAdapter.getPath().get(position));
        file.delete();
        mAdapter.refreshPath();
        mAdapter.notifyDataSetChanged();
    }

    private void downloadRecode() {
        MaterialInspectRecode localRecode = PicsLab.getPicsLab(getActivity()).getPicsByJobNum(mJobNum);
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
                        Gson gson = GsonUtil.getDateFormatGson();
                        RecodeDataPackage dataPackage = gson.fromJson(result, RecodeDataPackage.class);
                        MaterialInspectRecode serviceRecode = dataPackage.getRecode();
                        sRecodePics = findLaterRecode(localRecode, serviceRecode);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "记录加载完成！", Toast.LENGTH_SHORT).show();
                                mAdapter = new JobPicsAdapter(WorkingFragment.this,
                                        R.layout.inspect_image_item, sRecodePics.getImagesName(),
                                        sRecodePics.getType() + "/" + sRecodePics.getJobNum() + "/", mFolderPath);
                                mAdapter.setEditable(true);
                                if (!sRecodePics.getInspectorId()
                                        .equals(UserLab.getUserLab(getActivity()).getEmployee().getPhone())) {
                                    mEditable = false;
                                    setJobUnEdit();
                                } else {
                                    mEditable = true;
                                }
                                String inspector = OrgLab.getOrgLab(getActivity())
                                        .findEmployeeById(sRecodePics.getInspectorId()).getName();
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
     * 上传记录
     */
    private void uploadJob() {
        PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics, sRecodePics.getJobNum());
        Gson gson = GsonUtil.getDateFormatGson();
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
                            intent.putExtra("company_id", UserLab.getUserLab(getActivity()).getEmployee().getCompanyId());
                            intent.putExtra("recode_type", jobType);
                            intent.putExtra("folder_name", sRecodePics.getJobNum());
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
     * @param recode1 本地记录1
     * @param recode2 服务器记录2
     * @return laterRecode
     */
    private MaterialInspectRecode findLaterRecode(MaterialInspectRecode recode1
            , MaterialInspectRecode recode2) {
        if (recode1.getLatestUpdateTime() > recode2.getLatestUpdateTime()) {
            recode1.setImagesName(recode2.getImagesName());
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

    /***
     * 同步服务器与本地记录
     */
    private void synchronizeRecode() {
        ArrayList<String> urls = mAdapter.getPath();
        ArrayList<String> downloadUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            if (url.startsWith("http")) {
                downloadUrls.add(url);
            }
        }
        new VolumeImageDownLoad(downloadUrls, mFolderPath
                , new VolumeImageDownLoad.DownloadStateListener() {
            @Override
            public void onStart() {
                DialogUtil.showProgressDialog(getActivity(), mDialog, "正在同步记录...");
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onFinish() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "图片下载完成！", Toast.LENGTH_SHORT).show();
                        Log.e("mEditable:", mEditable + "");
                        if (mEditable) {
                            uploadJob();
                        }
                    }
                });

            }
        }).startDownload();
    }

    public void onKeyBackDown() {
        Log.e("isDataChanged", isDataChanged + "");
        if (isDataChanged) {
            Gson gson = GsonUtil.getDateFormatGson();
            String jobJson = gson.toJson(sRecodePics);
            RequestBody body = new FormBody.Builder()
                    .add("intent", "update")
                    .add("job_json", jobJson)
                    .build();
            new UploadData(getActivity(), HttpUtil.SERVLET_MATERIAL_RECODE, body, new UploadData.UploadStateListener() {
                @Override
                public void onStart() {
                    DialogUtil.showProgressDialog(getActivity(), mDialog, "正在保存数据，完成后退出，请稍后...");
                }

                @Override
                public void onFailed() {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(), "获取网络连接失败！请重试！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish(String result) {
                    mDialog.dismiss();
                    if (result.equals("OK")) {
                        PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics, sRecodePics.getJobNum());
                        isDataChanged = false;
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "数据上传失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                }
            }).upload();
        } else {
            getActivity().finish();
        }
    }

    protected void showContactsDialog(int requestCode) {
        String parentId = UserLab.getUserLab(getActivity()).getEmployee().getOrgId();
        FragmentManager fm = getFragmentManager();
        ChooseMemberDialog dialog = ChooseMemberDialog.newInstance(parentId);
        dialog.setTargetFragment(this, requestCode);
        dialog.show(fm, "date");
    }
}

