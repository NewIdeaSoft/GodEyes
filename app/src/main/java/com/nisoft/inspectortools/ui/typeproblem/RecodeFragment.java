package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.bean.problem.Recode;

/**
 * Created by NewIdeaSoft on 2017/7/2.
 */

public class RecodeFragment extends Fragment {

    private Recode mRecode;
//    private int layoutResId;


//    protected void init(){
//
//    }
//    public void updateData(){
//
//    }
//    public void updateView() {
//
//    }
//    protected View initView(){
//        return null;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        init();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = initView();
//        return view;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        updateView();
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        updateData();
//    }
    public void setRecode(Recode recode){
        mRecode = recode;
    }
    public Recode getRecode(){
        return mRecode;
    }
}
