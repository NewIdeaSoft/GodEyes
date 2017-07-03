package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
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
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.base.EditTextActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemSimpleInfoFragment extends Fragment {
    public static final int REQUEST_CODE_DISCOVERED_DATE = 11;
    public static final int REQUEST_CODE_HANDLED_DATE = 12;
    private ProblemRecode mProblem;
    private TextView mDiscoveredDate;
    private TextView mDiscover;
    private TextView mDiscoveredPosition;
    private TextView mTitle;
    private RecyclerView mImagesRecyclerView;
    private JobPicsAdapter mAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProblem = ProblemRecodeFragment1.getProblem().getProblem();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_simple_info, container, false);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDiscoveredDate = (TextView) view.findViewById(R.id.button_discovered_time);
        mDiscoveredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(REQUEST_CODE_DISCOVERED_DATE,new Date());
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
                startEditTextActivity(ProblemRecodeFragment1.REQUEST_DISCOVER_DESCRIPTION);
            }
        });
        mImagesRecyclerView = (RecyclerView) view.findViewById(R.id.problem_images_recycler_view);
        updateView();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        mImagesRecyclerView.setLayoutManager(layoutManager);
        String folder = ProblemRecodeFragment1.getProblem().getProblem().getImagesFolderPath();
        mAdapter = new JobPicsAdapter(ProblemSimpleInfoFragment.this,R.layout.problem_image_item,folder);
        mImagesRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private void startEditTextActivity(int requestCode) {
        Intent intent = new Intent(getActivity(), EditTextActivity.class);
        startActivityForResult(intent,requestCode);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateView() {
        if(mProblem.getTitle()!=null) {
            mTitle.setText(mProblem.getTitle());
        }
        if (mProblem.getDate() != null) {
            mDiscoveredDate.setText(dateFormat(mProblem.getDate()));
        }
        if (mProblem.getAuthor() != null) {
            mDiscover.setText(mProblem.getAuthor());
        }
        if (mProblem.getAddress() != null) {
            mDiscoveredPosition.setText(mProblem.getAddress());
        }
    }

    private String dateFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = format.format(date);
        return dateString;
    }

    private void showDatePickerDialog(int requestCode,Date date){
        FragmentManager fm = getFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(-1,date);
        dialog.setTargetFragment(ProblemSimpleInfoFragment.this,requestCode);
        dialog.show(fm,"date");
    }

    public void setProblem(ProblemRecode recode){
        mProblem = recode;
    }
}
