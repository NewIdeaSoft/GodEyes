package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.strings.FilePath;
import com.nisoft.inspectortools.utils.FileUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemInfoFragment extends RecodeFragment {
    public static final int REQUEST_DISCOVER = 101;
    public static final int REQUEST_DISCOVER_DESCRIPTION = 102;
    public static final int REQUEST_DISCOVER_DATE = 103;
    public static final int REQUEST_TITLE = 104;
    public static final int REQUEST_DISCOVER_ADDRESS = 105;
    private ProblemRecode mProblem;
    private TextView mDiscoveredDate;
    private TextView mDiscover;
    private TextView mDiscoveredPosition;
    private TextView mTitle;
    private TextView mDescriptionTextView;
    private RecyclerView mImagesRecyclerView;
    private JobPicsAdapter mAdapter;
    private String mFolderPath;


    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateView() {
        if (mProblem.getTitle() != null) {
            mTitle.setText(mProblem.getTitle());
        }
        if (mProblem.getDate() != null) {
            mDiscoveredDate.setText(StringFormatUtil.dateFormat(mProblem.getDate()));
        }
        if (mProblem.getAuthor() != null) {
            Employee discover = OrgLab.getOrgLab(getActivity()).findEmployeeById(mProblem.getAuthor());
            if(discover!=null) {
                mDiscover.setText(discover.getName());
            }
        }
        if (mProblem.getAddress() != null) {
            mDiscoveredPosition.setText(mProblem.getAddress());
        }
        if (mProblem.getDescription() != null) {
            mDescriptionTextView.setText(mProblem.getDescription());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        final Date updateDate = new Date();
        long updateTime = updateDate.getTime();
        mProblem.setUpdateTime(updateTime);
        switch (requestCode) {
            case 1:
                mAdapter.resetPath();
                mAdapter.notifyDataSetChanged();
                break;
            case REQUEST_DISCOVER:
                String discover = data.getStringExtra("author_name");
                String discoverId = data.getStringExtra("author_id");
                mProblem.setAuthor(discoverId);
                mDiscover.setText(discover);
                break;
            case REQUEST_DISCOVER_ADDRESS:
                String address = data.getStringExtra("content_edit");
                mProblem.setAddress(address);
                mDiscoveredPosition.setText(address);
                break;
            case REQUEST_DISCOVER_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                mProblem.setDate(date);
                mDiscoveredDate.setText(StringFormatUtil.dateFormat(date));
                break;
            case REQUEST_DISCOVER_DESCRIPTION:
                String description = data.getStringExtra("content_edit");
                mProblem.setDescription(description);
                mDescriptionTextView.setText(description);
                break;
            case REQUEST_TITLE:
                final String title = data.getStringExtra("content_edit");
                final String oldProblemFolderPath = ProblemRecodeFragment1.getProblemFolderPath();
                final String newProblemFolderPath = FilePath.PROBLEM_DATA_PATH + title +
                        "(" + mProblem.getRecodeId() + ")/";
                new AsyncTask<Void,Void,Boolean>(){
                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    @Override
                    protected void onPreExecute() {
                        dialog.setMessage("正在准备文件");
                        dialog.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        File oldProblemFolder = new File(oldProblemFolderPath);
                        File newProblemFolder = new File(newProblemFolderPath);
                        if(newProblemFolder.exists()){
                            FileUtil.deleteFile(newProblemFolder);
                        }
                        boolean result = oldProblemFolder.renameTo(newProblemFolder);
                        if (result){
                            FileUtil.deleteFile(oldProblemFolder);
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(Boolean success) {
                        if (success){
                            ProblemRecodeFragment1.setProblemFolderPath();
                            mFolderPath = newProblemFolderPath;
                            mAdapter.setRootPath(mFolderPath);
                            mAdapter.resetPath();
                            mAdapter.notifyDataSetChanged();
                            mProblem.setTitle(title);
                            mTitle.setText(title);
                            updateData();
                        }
                        dialog.dismiss();
                    }
                }.execute();
                break;
        }
        updateData();
    }

    public void setProblem(ProblemRecode recode) {
        mProblem = recode;
    }

    public ProblemRecode getProblem() {
        return mProblem;
    }

    public JobPicsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void init() {
        mProblem = ProblemRecodeFragment1.getProblem().getProblem();
        mFolderPath = ProblemRecodeFragment1.getProblemFolderPath()+"问题描述/";
    }

    @Override
    public void updateData() {
        ProblemDataPackage problem = ProblemRecodeFragment1.getProblem();
        problem.setProblem(mProblem);
        ProblemRecodeFragment1.setProblemData(problem);
        ProblemDataLab.getProblemDataLab(getActivity()).updateRecode(RecodeTable.PROBLEM_NAME,mProblem);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_simple_info, container, false);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_TITLE
                        , mTitle.getText().toString());
            }
        });
        mDiscoveredDate = (TextView) view.findViewById(R.id.button_discovered_time);
        mDiscoveredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(ProblemInfoFragment.this, REQUEST_DISCOVER_DATE, new Date());
            }
        });
        mDiscover = (TextView) view.findViewById(R.id.tv_discover);
        mDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsDialog(REQUEST_DISCOVER);
            }
        });
        mDiscoveredPosition = (TextView) view.findViewById(R.id.tv_discover_position);
        mDiscoveredPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_DISCOVER_ADDRESS,
                        mDiscoveredPosition.getText().toString());
            }
        });
        mDescriptionTextView = (TextView) view.findViewById(R.id.tv_description);
        mDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_DISCOVER_DESCRIPTION, mDescriptionTextView.getText().toString());
            }
        });
        mImagesRecyclerView = (RecyclerView) view.findViewById(R.id.problem_images_recycler_view);
        updateView();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mImagesRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new JobPicsAdapter(ProblemInfoFragment.this,
                R.layout.inspect_image_item,
                mProblem.getImagesNameOnServer(),
                "problem/"+mProblem.getRecodeId()+"/problem/",
                mFolderPath);
        mAdapter.setEditable(true);
        mImagesRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
