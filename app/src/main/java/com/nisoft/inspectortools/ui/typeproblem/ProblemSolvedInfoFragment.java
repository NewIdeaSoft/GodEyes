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


/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemSolvedInfoFragment extends Fragment {
    private TextView mSolvedText;
    private ImageRecode mProblem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_sovled_info,container,false);
        mSolvedText = (TextView) view.findViewById(R.id.tv_problem_program_info);
        mSolvedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mProblem = ProblemRecodeFragment1.getProblem().getResultRecode();
        if(mProblem.getDescription()!=null) {
            mSolvedText.setText(mProblem.getDescription());
        }
        return view;
    }

    public static ProblemSolvedInfoFragment newInstance(String problemId) {
        Bundle args = new Bundle();
        args.putSerializable(RecodeTable.Cols.PROBLEM_ID,problemId);
        ProblemSolvedInfoFragment fragment = new ProblemSolvedInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onPause() {
        super.onPause();
        updateProblem();
        Log.e("TAG", "Solved");
    }

    private void updateProblem(){
        mProblem.setDescription(mSolvedText.getText().toString());
        ProblemRecodeFragment1.getProblem().getProgram().setDescription(mSolvedText.getText().toString());
    }

    public ImageRecode getProblem() {
        return mProblem;
    }

    public void setProblem(ImageRecode problem) {
        mProblem = problem;
    }
}
