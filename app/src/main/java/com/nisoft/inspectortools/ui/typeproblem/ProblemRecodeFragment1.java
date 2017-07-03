package com.nisoft.inspectortools.ui.typeproblem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema.RecodeTable;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment1 extends Fragment {
    public static final int REQUEST_DISCOVER = 101;
    public static final int REQUEST_DISCOVER_DESCRIPTION = 102;
    public static final int REQUEST_DISCOVER_DATE = 103;
    public static final int REQUEST_TITLE = 104;
    public static final int REQUEST_DISCOVER_ADDRESS = 105;
    public static final int REQUEST_ANALYST = 201;
    public static final int REQUEST_ANALYSIS_DATE = 202;
    public static final int REQUEST_ANALYSIS_DESCRIPTION = 203;
    public static final int REQUEST_PROGRAM_AUTHOR = 301;
    public static final int REQUEST_PROGRAM_DATE = 302;
    public static final int REQUEST_PROGRAM_DESCRIPTION = 303;
    public static final int REQUEST_EXECUTOR = 401;
    public static final int REQUEST_EXECUTE_DATE = 402;
    public static final int REQUEST_EXECUTE_DESCRIPTION = 403;


    private static ProblemDataPackage mProblem;
    private ViewPager problemViewPager;
    private ArrayList<Fragment> problemFragmentList;
    private ProblemSimpleInfoFragment mProblemFragment;
    private ProblemAnalysisFragment mAnalysisFragment;
    private ProblemProgramFragment mProgramFragment;
    private ProblemSolvedInfoFragment mSolvedInfoFragment;
    private LinearLayout tab_problem_simple_info;
    private LinearLayout tab_problem_detailed_info;
    private LinearLayout tab_problem_reason_info;
    private LinearLayout tab_problem_solved_info;
    public static ProblemRecodeFragment1 newInstance(String problemId){
        Bundle args = new Bundle();
        args.putString(RecodeTable.Cols.PROBLEM_ID,problemId);
        ProblemRecodeFragment1 fragment = new ProblemRecodeFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater,container,savedInstanceState);
    }
    private View initView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_problem_recode1,container,false);
        ActionBar actionBar = getActivity().getActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(mProblem.getProblem().getTitle()!=null) {

        }else {

        }

        problemViewPager = (ViewPager) view.findViewById(R.id.problem_info_viewpager);
        FragmentManager fm = getFragmentManager();
        problemViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return problemFragmentList.get(position);
            }
            @Override
            public int getCount() {
                return problemFragmentList.size();
            }
        });
        problemViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resTabBackground();
                        tab_problem_simple_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 1:
                        resTabBackground();
                        tab_problem_detailed_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 2:
                        resTabBackground();
                        tab_problem_reason_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                    case 3:
                        resTabBackground();
                        tab_problem_solved_info.setBackgroundResource(R.color.colorTabSelect);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_problem_simple_info = (LinearLayout) view.findViewById(R.id.tab_problem_simple_info);
        tab_problem_detailed_info = (LinearLayout) view.findViewById(R.id.tab_problem_detailed_info);
        tab_problem_reason_info = (LinearLayout) view.findViewById(R.id.tab_problem_reason_info);
        tab_problem_solved_info = (LinearLayout) view.findViewById(R.id.tab_problem_sovled_info);

        tab_problem_simple_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_simple_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(0);
            }
        });
        tab_problem_detailed_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_detailed_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(1);
            }
        });
        tab_problem_reason_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_reason_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(2);
            }
        });
        tab_problem_solved_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resTabBackground();
                tab_problem_solved_info.setBackgroundResource(R.color.colorTabSelect);
                problemViewPager.setCurrentItem(3);
            }
        });
        return view;
    }

    private void initVariables(){
        problemFragmentList = new ArrayList<>();
        String problemId = getArguments().getString(RecodeTable.Cols.PROBLEM_ID);
        mProblem = ProblemDataLab.getProblemDataLab(getActivity()).getProblemById(problemId);
        mProblemFragment = new ProblemSimpleInfoFragment();
        mAnalysisFragment = new ProblemAnalysisFragment();
        mProgramFragment = new ProblemProgramFragment();
        mSolvedInfoFragment = new ProblemSolvedInfoFragment();
        problemFragmentList.add(mProblemFragment);
        problemFragmentList.add(mAnalysisFragment);
        problemFragmentList.add(mProgramFragment);
        problemFragmentList.add(mSolvedInfoFragment);
    }

    private void resTabBackground(){
        tab_problem_simple_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_detailed_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_reason_info.setBackgroundResource(R.color.colorTabBackgroung);
        tab_problem_solved_info.setBackgroundResource(R.color.colorTabBackgroung);
    }

    @Override
    public void onPause() {
        super.onPause();
        ProblemDataLab.getProblemDataLab(getActivity()).updateProblem(mProblem);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case REQUEST_DISCOVER :
                break;
            case REQUEST_DISCOVER_DESCRIPTION:
                String problemDescription = data.getStringExtra("content_edit");
                mProblem.getProblem().setDescription(problemDescription);
                mProblemFragment.setProblem( mProblem.getProblem());
                mProblemFragment.updateView();
                break;
            case REQUEST_DISCOVER_DATE :
                break;
            case REQUEST_TITLE :
                String problemTitle = data.getStringExtra("content_edit");
                mProblem.getProblem().setDescription(problemTitle);
                mProblemFragment.setProblem( mProblem.getProblem());
                mProblemFragment.updateView();
                break;
            case REQUEST_DISCOVER_ADDRESS :
                String address = data.getStringExtra("content_edit");
                mProblem.getProblem().setDescription(address);
                mProblemFragment.setProblem( mProblem.getProblem());
                mProblemFragment.updateView();
                break;
            case REQUEST_ANALYST:
                break;
            case REQUEST_ANALYSIS_DATE :
                break;
            case REQUEST_ANALYSIS_DESCRIPTION :
                break;
            case REQUEST_PROGRAM_AUTHOR :
                break;
            case REQUEST_PROGRAM_DATE :
                break;
            case REQUEST_PROGRAM_DESCRIPTION :
                break;
            case REQUEST_EXECUTOR :
                break;
            case REQUEST_EXECUTE_DATE :
                break;
            case REQUEST_EXECUTE_DESCRIPTION :
                break;

        }
    }

    public static ProblemDataPackage getProblem(){
        if(mProblem==null) {
            mProblem =new ProblemDataPackage();
        }
        return mProblem;
    }
}
