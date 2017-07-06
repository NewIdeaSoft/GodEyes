package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.ImageRecode;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;

import java.util.Date;


/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemResultFragment extends RecodeFragment {
    public static final int REQUEST_EXECUTOR = 401;
    public static final int REQUEST_EXECUTE_DATE = 402;
    public static final int REQUEST_EXECUTE_DESCRIPTION = 403;
    private TextView mSolvedText;
    private TextView mHandlerTextView;
    private TextView mDateTextView;
    private ImageRecode mProblem;

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
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setResultRecode(mProblem);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_sovled_info,container,false);
        mSolvedText = (TextView) view.findViewById(R.id.tv_problem_summary);
        mSolvedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_EXECUTE_DESCRIPTION,mSolvedText.getText().toString());
            }
        });
        if(mProblem.getDescription()!=null) {
            mSolvedText.setText(mProblem.getDescription());
        }

        mDateTextView = (TextView) view.findViewById(R.id.tv_program_executed_time);
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = mProblem.getDate();
                if (date ==null){
                    date = new Date();
                }
                showDatePickerDialog(ProblemResultFragment.this,REQUEST_EXECUTE_DATE,date);
            }
        });

        mHandlerTextView = (TextView) view.findViewById(R.id.tv_program_executed_member);
        mHandlerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_EXECUTOR,mHandlerTextView.getText().toString());
            }
        });
        return view;
    }
}
