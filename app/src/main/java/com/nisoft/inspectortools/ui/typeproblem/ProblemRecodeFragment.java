package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.PicsAdapter;
import com.nisoft.inspectortools.adapter.ProblemInfoItemAdapter;
import com.nisoft.inspectortools.bean.problem.Content;
import com.nisoft.inspectortools.bean.problem.Problem;
import com.nisoft.inspectortools.bean.problem.ProblemLab;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.ui.typeinspect.DatePickerDialog;
import com.nisoft.inspectortools.utils.FileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment extends Fragment {
    private static Problem mProblem;
    private EditText mTitileEdit;
    private RecyclerView mProblemImageRecycler;
    private PicsAdapter mPicsAdapter;
    private ArrayList<String> mPicsPath;
    private RecyclerView mProblemContentRecycler;
    private ProblemInfoItemAdapter mInfoItemAdapter;
    private ArrayList<Content> mProblemContents;
    public static ProblemRecodeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(ProblemTable.Cols.UUID, uuid);
        ProblemRecodeFragment fragment = new ProblemRecodeFragment();
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
        return initView(inflater, container);
    }

    private View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_problem_recode, container, false);
        mTitileEdit = (EditText) view.findViewById(R.id.problem_title_edit);
        mProblemImageRecycler = (RecyclerView) view.findViewById(R.id.problem_image_recycler);
        StaggeredGridLayoutManager imageLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        mProblemImageRecycler.setLayoutManager(imageLayoutManager);
        mProblemImageRecycler.setAdapter(mPicsAdapter);

        mProblemContentRecycler = (RecyclerView) view.findViewById(R.id.problem_content_recycler);
        StaggeredGridLayoutManager contentLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mProblemContentRecycler.setLayoutManager(contentLayoutManager);
        mProblemContentRecycler.setAdapter(mInfoItemAdapter);
        return view;
    }

    private void initVariables() {
        UUID uuid = (UUID) getArguments().getSerializable(ProblemTable.Cols.UUID);
        mProblem = ProblemLab.getProblemLab(getActivity()).getProblem(uuid);
        mPicsPath = mProblem.getPhotoPath();
        mPicsAdapter = new PicsAdapter(this,mPicsPath);
        mProblemContents = Content.getContentsFromProblem(mProblem);
        mInfoItemAdapter = new ProblemInfoItemAdapter(mProblemContents,getActivity());



    }

    @Override
    public void onPause() {
        super.onPause();
        ProblemLab.getProblemLab(getActivity()).updateProblem(getProblem());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPicsAdapter.notifyDataSetChanged();
        mInfoItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 0:
//                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
//                sRecodePics.setDate(date);
//                mDatePickerButton.setText(dateFormat(sRecodePics.getDate()));
                break;
            case 1:
                final String path = data.getStringExtra("PhotoPath");
                final String resourcePhotoPath = data.getStringExtra("resourcePhotoPath");
                final int position = data.getIntExtra("position",-1);
                if (path != null&&position>-1) {
                    if (position<mPicsPath.size()){
                        mPicsPath.set(position,path);
                    }else {
                        mPicsPath.add(path);
                    }
                    if(resourcePhotoPath!=null){
                        Snackbar.make(mProblemImageRecycler,"已移动照片至"+path+",点击撤销",Snackbar.LENGTH_LONG)
                                .setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FileUtil.moveFile(path,resourcePhotoPath);
                                        mPicsPath.remove(position);
                                        mPicsAdapter.notifyDataSetChanged();
                                    }
                                })
                                .show();
                    }


                }else if(path==null){
                    Toast.makeText(getActivity(),"照片已存在，请重新选择！",Toast.LENGTH_LONG).show();
                }
                mPicsAdapter.setPicsPath(mPicsPath);
                mProblem.setPhotoPath(mPicsPath);
                break;
        }
    }

    public static Problem getProblem() {
        if (mProblem == null) {
            mProblem = new Problem();
        }
        return mProblem;
    }
}
