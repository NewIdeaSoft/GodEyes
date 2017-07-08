package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Recode;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.Date;

/**
 * Created by NewIdeaSoft on 2017/7/2.
 */

public class ProblemProgramFragment extends RecodeFragment {
    public static final int REQUEST_PROGRAM_AUTHOR = 301;
    public static final int REQUEST_PROGRAM_DATE = 302;
    public static final int REQUEST_PROGRAM_DESCRIPTION = 303;

    private TextView mAuthorTextView;
    private TextView mDateTextView;
    private TextView mDescriptionTextView;
    private Recode mProgram;

    public Recode getProgram() {
        return mProgram;
    }

    public void setProgram(Recode program) {
        mProgram = program;
    }

    @Override
    protected void init() {
        mProgram = ProblemRecodeFragment1.getProblem().getProgram();
    }

    @Override
    public void updateData() {
        ProblemRecodeFragment1.getProblem().setProgram(mProgram);
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_program,container,false);
        mAuthorTextView = (TextView) view.findViewById(R.id.tv_program_author);
        mDateTextView=(TextView) view.findViewById(R.id.tv_program_design_time);
        mDescriptionTextView=(TextView) view.findViewById(R.id.tv_problem_program_info);
        mAuthorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = mProgram.getDate();
                if (date ==null){
                    date = new Date();
                }
                showDatePickerDialog(ProblemProgramFragment.this,REQUEST_PROGRAM_DATE,date);
            }
        });
        mDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditTextActivity(REQUEST_PROGRAM_DESCRIPTION,mDescriptionTextView.getText().toString());
            }
        });
        updateView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        mProgram.setUpdateTime(new Date().getTime());
        switch(requestCode){
            case REQUEST_PROGRAM_AUTHOR:
                break;
            case REQUEST_PROGRAM_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                mProgram.setDate(date);
                break;
            case REQUEST_PROGRAM_DESCRIPTION:
                String description = data.getStringExtra("content_edit");
                mProgram.setDescription(description);
                break;
        }
        updateView();
    }
    private void updateView(){
        if (mProgram.getAuthor()!=null){
            mAuthorTextView.setText(mProgram.getAuthor());
        }
        if (mProgram.getDate()!=null){
            mDateTextView.setText(StringFormatUtil.dateFormat(mProgram.getDate()));
        }
        if (mProgram.getDescription()!=null){
            mDescriptionTextView.setText(mProgram.getDescription());
        }
    }
}
