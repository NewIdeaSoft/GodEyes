package com.nisoft.inspectortools.ui.typeproblem;

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
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.org.UserLab;
import com.nisoft.inspectortools.bean.problem.ImageRecode;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;
import com.nisoft.inspectortools.service.FileUploadService;
import com.nisoft.inspectortools.ui.strings.FilePath;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.HttpUtil;
import com.nisoft.inspectortools.utils.VolumeImageDownLoad;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment1 extends Fragment {

    public static final String TAG = "ProblemRecodeFragment1:";

    private String mProblemId;

    private static ProblemDataPackage sProblemData;
    private ViewPager problemViewPager;
    private ArrayList<RecodeFragment> problemFragmentList;
    private LinearLayout tab_problem_info;
    private LinearLayout tab_problem_reason;
    private LinearLayout tab_problem_program;
    private LinearLayout tab_problem_result;
    private LinearLayout line_problem_info;
    private LinearLayout line_problem_reason;
    private LinearLayout line_problem_program;
    private LinearLayout line_problem_result;
    private TextView text_problem_info;
    private TextView text_problem_reason;
    private TextView text_problem_program;
    private TextView text_problem_result;

    private FragmentStatePagerAdapter mPagerAdapter;
    private static String sProblemFolderPath;
    private ProgressDialog mDialog;
    private boolean mEditable = true;

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
        if (sProblemData.getProblem().getTitle() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(sProblemData.getProblem().getTitle());
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("新增记录");
        }
        mDialog = new ProgressDialog(getActivity());
        problemViewPager = (ViewPager) view.findViewById(R.id.problem_info_viewpager);

        tab_problem_info = (LinearLayout) view.findViewById(R.id.tab_problem_info);
        tab_problem_reason = (LinearLayout) view.findViewById(R.id.tab_problem_reason);
        tab_problem_program = (LinearLayout) view.findViewById(R.id.tab_problem_program);
        tab_problem_result = (LinearLayout) view.findViewById(R.id.tab_problem_result);
        line_problem_info = (LinearLayout) view.findViewById(R.id.line_problem_info);
        line_problem_reason = (LinearLayout) view.findViewById(R.id.line_problem_reason);
        line_problem_program = (LinearLayout) view.findViewById(R.id.line_problem_program);
        line_problem_result = (LinearLayout) view.findViewById(R.id.line_problem_result);
        text_problem_info = (TextView) view.findViewById(R.id.text_problem_info);
        text_problem_reason= (TextView) view.findViewById(R.id.text_problem_reason);
        text_problem_program= (TextView) view.findViewById(R.id.text_problem_program);
        text_problem_result= (TextView) view.findViewById(R.id.text_problem_result);

        tab_problem_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                line_problem_info.setBackgroundResource(R.color.colorTabSelect);
                text_problem_info.setTextColor(getResources().getColor(R.color.colorTabSelect));
                problemViewPager.setCurrentItem(0);
            }
        });
        tab_problem_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                line_problem_reason.setBackgroundResource(R.color.colorTabSelect);
                text_problem_reason.setTextColor(getResources().getColor(R.color.colorTabSelect));
                problemViewPager.setCurrentItem(1);
            }
        });
        tab_problem_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                line_problem_program.setBackgroundResource(R.color.colorTabSelect);
                text_problem_program.setTextColor(getResources().getColor(R.color.colorTabSelect));
                problemViewPager.setCurrentItem(2);
            }
        });
        tab_problem_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                line_problem_result.setBackgroundResource(R.color.colorTabSelect);
                text_problem_result.setTextColor(getResources().getColor(R.color.colorTabSelect));
                problemViewPager.setCurrentItem(3);
            }
        });
        downloadRecode();
        return view;
    }

    private void initVariables() {
        problemFragmentList = new ArrayList<>();
        mProblemId = getArguments().getString(RecodeTable.Cols.PROBLEM_ID);
        Log.e(TAG, mProblemId);
        sProblemData = ProblemDataLab.getProblemDataLab(getActivity()).getProblemById(mProblemId);
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
//        Log.e(TAG,gson.toJson(sProblemData));
        setProblemFolderPath();
    }

    private void resTabBackground() {
        line_problem_info.setBackgroundResource(R.color.colorTabBackgroung);
        line_problem_reason.setBackgroundResource(R.color.colorTabBackgroung);
        line_problem_program.setBackgroundResource(R.color.colorTabBackgroung);
        line_problem_result.setBackgroundResource(R.color.colorTabBackgroung);
        text_problem_info.setTextColor(getResources().getColor(R.color.colorTabBackgroung));
        text_problem_reason.setTextColor(getResources().getColor(R.color.colorTabBackgroung));
        text_problem_program.setTextColor(getResources().getColor(R.color.colorTabBackgroung));
        text_problem_result.setTextColor(getResources().getColor(R.color.colorTabBackgroung));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        ArrayList<String> urls = ((ProblemInfoFragment)problemFragmentList.get(0)).getAdapter().getPath();
        ArrayList<String> downloadUrls = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            if (url.startsWith("http")) {
                downloadUrls.add(url);
            }
        }
        new VolumeImageDownLoad(downloadUrls, sProblemFolderPath+"问题描述/"
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
        ProblemDataLab.getProblemDataLab(getActivity()).updateProblem(sProblemData);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String jobJson = gson.toJson(sProblemData);
        Log.e("sProblemData",jobJson);
        RequestBody body = new FormBody.Builder()
                .add("intent", "update")
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
                            intent.putExtra("folder_path", sProblemFolderPath+"/problem/");
                            intent.putExtra("company_id", UserLab.getUserLab(getActivity()).getEmployee().getCompanyId());
                            intent.putExtra("recode_type","problem");
                            intent.putExtra("folder_name", sProblemData.getProblem().getRecodeId()+"/problem");
                            getActivity().startService(intent);
                        } else {
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            Log.e("uploadProblem",result);
                        }
                    }
                });

            }
        });
    }

    public static ProblemDataPackage getProblem() {
        if (sProblemData == null) {
            sProblemData = new ProblemDataPackage();
        }
        return sProblemData;
    }
    public static void setProblemData(ProblemDataPackage problemData){
        sProblemData = problemData;
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
                        Log.e("download:", result);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        ProblemDataPackage serviceRecode = gson.fromJson(result, ProblemDataPackage.class);
                        sProblemData = findLaterRecode(localProblemData, serviceRecode);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setProblemFragmentList();
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "记录加载完成！", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
    }
    private ProblemDataPackage findLaterRecode(ProblemDataPackage localProblemData
            , ProblemDataPackage serviceRecode) {
        if (localProblemData.getProblem().getUpdateTime() > serviceRecode.getProblem().getUpdateTime()) {
            ProblemRecode problemRecode = localProblemData.getProblem();
            ImageRecode resultRecode = localProblemData.getResultRecode();
            ArrayList<String> problemImagesName = serviceRecode.getProblem().getImagesNameOnServer();
            Log.e("localProblemData:",problemImagesName.toString());
            ArrayList<String> resultImagesName = serviceRecode.getResultRecode().getImagesNameOnServer();
            problemRecode.setImagesNameOnserver(problemImagesName);
            resultRecode.setImagesNameOnserver(resultImagesName);
            localProblemData.setProblem(problemRecode);
            localProblemData.setResultRecode(resultRecode);
            return localProblemData;
        }
        Log.e("serviceRecode:",serviceRecode.getProblem().getImagesNameOnServer().toString());
        return serviceRecode;
    }
    private void setProblemFragmentList(){
        problemFragmentList.add(new ProblemInfoFragment());
        problemFragmentList.add(new ProblemAnalysisFragment());
        problemFragmentList.add(new ProblemProgramFragment());
        problemFragmentList.add(new ProblemResultFragment());
        FragmentManager fm = getFragmentManager();
        mPagerAdapter = new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return problemFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return problemFragmentList.size();
            }
        };
        problemViewPager.setAdapter(mPagerAdapter);

        problemViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resTabBackground();
                        line_problem_info.setBackgroundResource(R.color.colorTabSelect);
                        text_problem_info.setTextColor(getResources().getColor(R.color.colorTabSelect));
                        break;
                    case 1:
                        resTabBackground();
                        line_problem_reason.setBackgroundResource(R.color.colorTabSelect);
                        text_problem_reason.setTextColor(getResources().getColor(R.color.colorTabSelect));
                        break;
                    case 2:
                        resTabBackground();
                        line_problem_program.setBackgroundResource(R.color.colorTabSelect);
                        text_problem_program.setTextColor(getResources().getColor(R.color.colorTabSelect));
                        break;
                    case 3:
                        resTabBackground();
                        line_problem_result.setBackgroundResource(R.color.colorTabSelect);
                        text_problem_result.setTextColor(getResources().getColor(R.color.colorTabSelect));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static String getProblemFolderPath() {
        return sProblemFolderPath;
    }

    public static void setProblemFolderPath() {
        sProblemFolderPath  = FilePath.PROBLEM_DATA_PATH + sProblemData.getProblem().getTitle() +
                "(" + sProblemData.getProblem().getRecodeId() + ")/";
    }

}
