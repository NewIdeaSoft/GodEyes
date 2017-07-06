package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.strings.FilePath;
import com.nisoft.inspectortools.utils.StringFormatUtil;

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
            mDiscover.setText(mProblem.getAuthor());
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
        switch (requestCode) {
            case REQUEST_DISCOVER:
                String discover = data.getStringExtra("content_edit");
                mProblem.setAuthor(discover);
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
                String title = data.getStringExtra("content_edit");
                mProblem.setTitle(title);
                mTitle.setText(title);
                break;
        }
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
        mFolderPath = FilePath.PROBLEM_DATA_PATH + mProblem.getTitle() +
                "(" + mProblem.getRecodeId() + ")/问题描述/";
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setProblem(mProblem);
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

            }
        });
        mDiscoveredPosition = (TextView) view.findViewById(R.id.tv_discover_position);
        mDiscoveredPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_DISCOVER_DESCRIPTION,
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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        mImagesRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new JobPicsAdapter(ProblemInfoFragment.this, R.layout.problem_image_item, mFolderPath);
        mAdapter.setEditable(true);
        mImagesRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
