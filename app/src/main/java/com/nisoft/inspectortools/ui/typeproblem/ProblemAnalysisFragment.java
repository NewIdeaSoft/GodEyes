package com.nisoft.inspectortools.ui.typeproblem;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Recode;

import java.util.Date;


/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemAnalysisFragment extends RecodeFragment {
    public static final int REQUEST_ANALYST = 201;
    public static final int REQUEST_ANALYSIS_DATE = 202;
    public static final int REQUEST_ANALYSIS_DESCRIPTION = 203;
    private TextView mAnalyserTextView;
    private TextView mAnalystDateTextView;
    private TextView reasonText;
    private Recode mProblem;

    @Override
    public void onPause() {
        super.onPause();
    }

    public Recode getProblem() {
        return mProblem;
    }

    public void setProblem(Recode problem) {
        mProblem = problem;
    }

    @Override
    protected void init() {
        mProblem = ProblemRecodeFragment1.getProblem().getAnalysis();
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setAnalysis(mProblem);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_reason_info, container, false);
        mAnalyserTextView = (TextView) view.findViewById(R.id.tv_analyser);
        mAnalyserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_ANALYST, mAnalyserTextView.getText().toString());
            }
        });
        mAnalystDateTextView = (TextView) view.findViewById(R.id.tv_analyser_time);
        mAnalystDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = mProblem.getDate();
                if (date == null) {
                    date = new Date();
                }
                showDatePickerDialog(ProblemAnalysisFragment.this, REQUEST_ANALYSIS_DATE, date);
            }
        });
        reasonText = (TextView) view.findViewById(R.id.edit_problem_reason_info);
        reasonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_ANALYSIS_DESCRIPTION, reasonText.getText().toString());
            }
        });

        if (mProblem.getDescription() != null) {
            reasonText.setText(mProblem.getDescription());
        }
        return view;
    }
}
