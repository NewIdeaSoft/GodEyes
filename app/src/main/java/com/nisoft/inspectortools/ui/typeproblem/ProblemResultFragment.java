package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.org.Employee;
import com.nisoft.inspectortools.bean.org.OrgLab;
import com.nisoft.inspectortools.bean.problem.ImageRecode;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.Date;


/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public abstract class ProblemResultFragment extends RecodeFragment {
    public static final int REQUEST_EXECUTOR = 401;
    public static final int REQUEST_EXECUTE_DATE = 402;
    public static final int REQUEST_EXECUTE_DESCRIPTION = 403;
    private TextView mSolvedText;
    private TextView mHandlerTextView;
    private TextView mDateTextView;
    private ImageRecode mProblem;
    private RecyclerView mResultRecyclerView;
    private JobPicsAdapter mAdapter;
    private String mFolderPath;

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "Solved");
    }

    public ImageRecode getProblem() {
        return mProblem;
    }

    public void setProblem(ImageRecode problem) {
        mProblem = problem;
    }

    @Override
    protected void init() {
        mProblem = ProblemRecodeFragment1.getProblem().getResultRecode();
        mFolderPath = ProblemRecodeFragment1.getProblemFolderPath() + "处理结果/";
        Log.e("JobPicsAdapter:", mFolderPath);
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setResultRecode(mProblem);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_sovled_info, container, false);
        mSolvedText = (TextView) view.findViewById(R.id.tv_problem_summary);
        mSolvedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_EXECUTE_DESCRIPTION, mSolvedText.getText().toString());
            }
        });

        mDateTextView = (TextView) view.findViewById(R.id.tv_program_executed_time);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = mProblem.getDate();
                if (date == null) {
                    date = new Date();
                }
                showDatePickerDialog(ProblemResultFragment.this, REQUEST_EXECUTE_DATE, date);
            }
        });

        mHandlerTextView = (TextView) view.findViewById(R.id.tv_program_executed_member);
        mHandlerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsDialog(REQUEST_EXECUTOR);
            }
        });
        mResultRecyclerView = (RecyclerView) view.findViewById(R.id.result_image_recycler_view);
        updateView();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mResultRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new JobPicsAdapter(ProblemResultFragment.this,
                R.layout.inspect_image_item,
                mProblem.getImagesNameOnServer(),
                "problem/" + mProblem.getRecodeId() + "/result/",
                mFolderPath);
        mAdapter.setEditable(true);
        mResultRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void updateView() {
        if (mProblem.getAuthor() != null) {
            Employee discover = OrgLab.getOrgLab(getActivity()).findEmployeeById(mProblem.getAuthor());
            if (discover != null) {
                mHandlerTextView.setText(discover.getName());
            }
        }
        if (mProblem.getDate() != null) {
            mDateTextView.setText(StringFormatUtil.dateFormat(mProblem.getDate()));
        }
        if (mProblem.getDescription() != null) {
            mSolvedText.setText(mProblem.getDescription());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                mAdapter.resetPath();
                mAdapter.notifyDataSetChanged();
                mProblem.setUpdateTime(new Date().getTime());
                break;
            case REQUEST_EXECUTOR:
                String discoverId = data.getStringExtra("author_id");
                if (!discoverId.equals(mProblem.getAuthor())){
                    mProblem.setAuthor(discoverId);
                    mProblem.setUpdateTime(new Date().getTime());
                    onDataChanged();
                }
                break;
            case REQUEST_EXECUTE_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                if (!date.equals(mProblem.getDate())){
                    mProblem.setDate(date);
                    mProblem.setUpdateTime(new Date().getTime());
                    onDataChanged();
                }
                break;
            case REQUEST_EXECUTE_DESCRIPTION:
                String description = data.getStringExtra("content_edit");
                mProblem.setDescription(description);
                mProblem.setUpdateTime(new Date().getTime());
                onDataChanged();
                break;
        }
        updateView();
    }
}
