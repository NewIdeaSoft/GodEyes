package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.managertools.R;
import com.nisoft.managertools.db.problem.RecodeDbSchema;
import com.nisoft.managertools.entity.problem.Recode;

/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemReasonInfoFragment extends Fragment {
    private TextView mAnalyserTextView;
    private TextView mAnalystDateTextView;
    private TextView reasonText;
    private Recode mProblem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_reason_info, container, false);
        mAnalyserTextView = (TextView) view.findViewById(R.id.tv_analyser);
        mAnalyserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mAnalystDateTextView = (TextView) view.findViewById(R.id.tv_analyser_time);
        mAnalystDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reasonText = (TextView) view.findViewById(R.id.edit_problem_reason_info);

//        mProblem = ProblemLab.getProblemLab(getActivity()).getProblem((UUID) getArguments().getSerializable(ProblemTable.Cols.UUID));
        mProblem = ProblemRecodeFragment.getProblem().getAnalysis();
        if (mProblem.getDescription() != null) {
            reasonText.setText(mProblem.getDescription());
        }
        return view;
    }

    public static ProblemReasonInfoFragment newInstance(String problemId) {
        Bundle args = new Bundle();
        args.putSerializable(RecodeDbSchema.RecodeTable.Cols.PROBLEM_ID, problemId);
        ProblemReasonInfoFragment fragment = new ProblemReasonInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateProblem();
    }

    private void updateProblem() {
        mProblem.setDescription(reasonText.getText().toString());
        ProblemRecodeFragment.getProblem().getAnalysis().setDescription(reasonText.getText().toString());
    }
}
