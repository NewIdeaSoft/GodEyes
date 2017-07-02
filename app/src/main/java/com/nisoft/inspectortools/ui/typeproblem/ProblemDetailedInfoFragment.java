package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.JobPicsAdapter;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.utils.FileUtil;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/4/26.
 */

public class ProblemDetailedInfoFragment extends Fragment {
    private RecyclerView mProblemPhotoList;
    private EditText mProblemDetailedInfo;
    private JobPicsAdapter mAdapter;
    private ArrayList<String> mPhotosPath;
    private ProblemRecode mProblem;

    public static final int PICTURE_REQUEST = 0;
    private void init(){
        mProblem = ProblemRecodeFragment1.getProblem().getProblem();
        String folderPath = mProblem.getImagesFolderPath();
        mPhotosPath = FileUtil.getImagesPath(folderPath);

        if(mPhotosPath == null) {
            mPhotosPath = new ArrayList<>();
        }

    }
    private void initView(View view){
        mProblemPhotoList = (RecyclerView) view.findViewById(R.id.problem_photo_list);
        mProblemDetailedInfo = (EditText) view.findViewById(R.id.edit_problem_detailed_info);
        if(mProblem.getDescription()!=null) {
            mProblemDetailedInfo.setText(mProblem.getDescription());
        }
        mProblemDetailedInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) {
                    mProblem.setDescription(s.toString());
                    ProblemRecodeFragment1.getProblem().getProblem().setDescription(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initPhotos(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        mProblemPhotoList.setLayoutManager(layoutManager);
        String folder = ProblemRecodeFragment1.getProblem().getProblem().getImagesFolderPath();
        mAdapter = new JobPicsAdapter(ProblemDetailedInfoFragment.this,R.layout.problem_image_item,folder);
        mProblemPhotoList.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detailed_info,container,false);
        initView(view);
        initPhotos();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICTURE_REQUEST :
                String path = data.getStringExtra("PhotoPath");
                if(path!=null) {
                    mPhotosPath.add(path);
                }
                mAdapter.refreshPath();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
//        ProblemLab.getProblemLab(getActivity()).updateProblem(mProblem);
        Log.e("TAG", "Detailed");
    }

}
