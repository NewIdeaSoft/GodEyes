package com.nisoft.inspectortools.ui.typeproblem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.inspect.MaterialInspectRecode;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;
import com.nisoft.inspectortools.gson.RecodeDataPackage;
import com.nisoft.inspectortools.service.FileUploadService;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.typeinspect.WorkingFragment;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.FileUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;
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

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment1 extends Fragment {
    public static final int REQUEST_ANALYST = 201;
    public static final int REQUEST_ANALYSIS_DATE = 202;
    public static final int REQUEST_ANALYSIS_DESCRIPTION = 203;
    public static final int REQUEST_PROGRAM_AUTHOR = 301;
    public static final int REQUEST_PROGRAM_DATE = 302;
    public static final int REQUEST_PROGRAM_DESCRIPTION = 303;
    public static final int REQUEST_EXECUTOR = 401;
    public static final int REQUEST_EXECUTE_DATE = 402;
    public static final int REQUEST_EXECUTE_DESCRIPTION = 403;

    public static final String TAG = "ProblemRecodeFragment1:";

    private String mProblemId;

    private static ProblemDataPackage mProblemData;
    private ViewPager problemViewPager;
    private ArrayList<Fragment> problemFragmentList;
    private ProblemSimpleInfoFragment mProblemFragment;
    private ProblemAnalysisFragment mAnalysisFragment;
    private ProblemProgramFragment mProgramFragment;
    private ProblemSolvedInfoFragment mSolvedInfoFragment;
    private LinearLayout tab_problem_simple_info;
    private LinearLayout tab_problem_detailed_info;
    private LinearLayout tab_problem_reason_info;
    private LinearLayout tab_problem_solved_info;
    private String mFolderPath;
    private ProgressDialog mDialog;
    private boolean mEditable;

    public static ProblemRecodeFragment1 newInstance(String problemId) {
        Bundle args = new Bundle();
        args.putString(RecodeTable.Cols.PROBLEM_ID, problemId);
        ProblemRecodeFragment1 fragment = new ProblemRecodeFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    private View initView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_recode1, container, false);
        setHasOptionsMenu(true);
        if (mProblemData.getProblem().getTitle() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mProblemData.getProblem().getTitle());
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("新增记录");
        }
        mDialog = new ProgressDialog(getActivity());
        problemViewPager = (ViewPager) view.findViewById(R.id.problem_info_viewpager);
        FragmentManager fm = getFragmentManager();
        problemViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return problemFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return problemFragmentList.size();
            }
        });
        problemViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        mProblemData.setProblem(mProblemFragment.getProblem());
                        break;
                    case 1:
                        mProblemData.setAnalysis(mAnalysisFragment.getProblem());
                        break;
                    case 2:
                        mProblemData.setProgram(mProgramFragment.getProgram());
                        break;
                    case 3:
                        mProblemData.setResultRecode(mSolvedInfoFragment.getProblem());
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resTabBackground();
                        tab_problem_simple_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 1:
                        resTabBackground();
                        tab_problem_detailed_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 2:
                        resTabBackground();
                        tab_problem_reason_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 3:
                        resTabBackground();
                        tab_problem_solved_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_problem_simple_info = (LinearLayout) view.findViewById(R.id.tab_problem_simple_info);
        tab_problem_detailed_info = (LinearLayout) view.findViewById(R.id.tab_problem_detailed_info);
        tab_problem_reason_info = (LinearLayout) view.findViewById(R.id.tab_problem_reason_info);
        tab_problem_solved_info = (LinearLayout) view.findViewById(R.id.tab_problem_sovled_info);

        tab_problem_simple_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_simple_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(0);
            }
        });
        tab_problem_detailed_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_detailed_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(1);
            }
        });
        tab_problem_reason_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_reason_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(2);
            }
        });
        tab_problem_solved_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_solved_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(3);
            }
        });
        return view;
    }

    private void initVariables() {
        problemFragmentList = new ArrayList<>();
        mProblemId = getArguments().getString(RecodeTable.Cols.PROBLEM_ID);
        Log.e(TAG, mProblemId);
        mProblemData = ProblemDataLab.getProblemDataLab(getActivity()).getProblemById(mProblemId);
        Gson gson = new Gson();
        Log.e(TAG,gson.toJson(mProblemData));
        mProblemFragment = new ProblemSimpleInfoFragment();
        mAnalysisFragment = new ProblemAnalysisFragment();
        mProgramFragment = new ProblemProgramFragment();
        mSolvedInfoFragment = new ProblemSolvedInfoFragment();
        problemFragmentList.add(mProblemFragment);
        problemFragmentList.add(mAnalysisFragment);
        problemFragmentList.add(mProgramFragment);
        problemFragmentList.add(mSolvedInfoFragment);
    }

    private void resTabBackground() {
        tab_problem_simple_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_detailed_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_reason_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_solved_info.setBackgroundResource(R.color.colorTabBackgroung);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        ProblemDataLab.getProblemDataLab(getActivity()).updateProblem(mProblemData);
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
//                String data = m.toString();
//                //在分线程写入字符串到指定目录的文件下
//                File file = new File(mFolderPath);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                FileUtil.writeStringToFile(data, mFolderPath + sRecodePics.getJobNum() + ".txt");
//                Toast.makeText(getActivity(), "导出数据完成！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.data_share:
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, sRecodePics.toString());
//                startActivity(i);
                break;
            case R.id.data_upload:
                synchronizeRecode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * 同步服务器与本地记录
     */
    private void synchronizeRecode() {
        ArrayList<String> urls = mProblemFragment.getAdapter().getPath();
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
                            uploadProblem();
                        }
                    }
                });

            }
        }).startDownload();

    }

    private void uploadProblem() {
        ProblemDataLab.getProblemDataLab(getActivity()).updateProblem(mProblemData);
        Gson gson = new Gson();
        String jobJson = gson.toJson(mProblemData);
        RequestBody body = new FormBody.Builder()
                .add("intent", "upload")
                .add("job_json", jobJson)
                .build();

        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在上传数据...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_PROBLEM_RECODE, body, new Callback() {
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
                            Toast.makeText(getActivity(), "数据上传完成，开始上传照片！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), FileUploadService.class);
                            intent.putExtra("folder_path", mFolderPath);
                            intent.putExtra("company_id", UserLab.getUserLab(getActivity()).getEmployee().getCompanyId());
                            intent.putExtra("recode_type",mProblemData.getProblem().getType());
                            getActivity().startService(intent);
                        } else {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    public static ProblemDataPackage getProblem() {
        if (mProblemData == null) {
            mProblemData = new ProblemDataPackage();
        }
        return mProblemData;
    }
    private void downloadRecode() {
        ProblemDataPackage localRecode = ProblemDataLab.getProblemDataLab(getActivity()).getProblemById(mProblemId);
        downloadRecodeFromServer(localRecode);
    }

    /***
     * 从服务器下载记录，和本地记录的时间戳比较，设置记录为最新记录
     * @param localProblemData 本地记录
     */
    private void downloadRecodeFromServer(final ProblemDataPackage localProblemData) {
        RequestBody body = new FormBody.Builder()
                .add("intent", "recoding")
                .add("problem_id", mProblemId)
                .build();
        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在加载记录...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_PROBLEM_RECODE
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
                        Log.e(TAG, result);
                        Gson gson = new Gson();
                        ProblemDataPackage serviceRecode = gson.fromJson(result, ProblemDataPackage.class);
                        final String inspector = serviceRecode.getProblem().getAuthor();
                        mProblemData = findLaterRecode(localProblemData, serviceRecode);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "记录加载完成！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }
    private ProblemDataPackage findLaterRecode(ProblemDataPackage recode1
            , ProblemDataPackage recode2) {
        if (recode1.getProblem().getUpdateTime() > recode2.getProblem().getUpdateTime()) {
            return recode1;
        }
        return recode2;
    }
}
