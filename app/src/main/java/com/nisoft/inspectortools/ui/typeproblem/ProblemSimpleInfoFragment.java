package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;

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
    RecyclerView mImagesRecyclerView;

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
                showDatePickerDialog(REQUEST_CODE_DISCOVERED_DATE,"选择发现时间");
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

            }
        });
        mImagesRecyclerView = (RecyclerView) view.findViewById(R.id.problem_images_recycler_view);
        updateView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) {
            return;
        }
        Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
        switch (requestCode) {
            case  REQUEST_CODE_DISCOVERED_DATE:
                ProblemRecodeFragment1.getProblem().getProblem().setDate(date);
//                mProblem.setDate(date);
//                SingleProblem.getSingleProblem(getActivity()).getProblem().setDate(date);
                mDiscoveredDate.setText(dateFormat(date));
                break;
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        updateProblemInfo();
//        ProblemLab.getProblemLab(getActivity()).updateProblem(mProblem);
        Log.e("TAG",mProblem.getAuthor());
        Log.e("TAG",mProblem.getDate().toString());
        Log.e("TAG",mProblem.getAddress());
        Log.e("TAG","simple");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateView() {
        mProblem = ProblemRecodeFragment1.getProblem().getProblem();
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

    private void showDatePickerDialog(int requestCode,String title){
        FragmentManager fm = getFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(title);
        dialog.setTargetFragment(ProblemSimpleInfoFragment.this,requestCode);
        dialog.show(fm,"date");
    }

    private void updateProblemInfo(){

        ProblemRecodeFragment1.getProblem().getProblem().setTitle(mTitle.getText().toString());
        ProblemRecodeFragment1.getProblem().getProblem().setAuthor(mDiscover.getText().toString());
        ProblemRecodeFragment1.getProblem().getProblem().setAddress(mDiscoveredPosition.getText().toString());
    }
}
