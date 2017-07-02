package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by NewIdeaSoft on 2017/7/2.
 */

public class RecodeFragment extends Fragment {

    private int layoutResId;
    private StartEdit iStartEdit;

    protected void setOnTextClickListener(StartEdit startEdit){
        iStartEdit = startEdit;
    }

    protected void init(){

    }
    public void updateData(){

    }
    private void updateView() {

    }
    protected View initView(View view){
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResId,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateData();
    }

    public interface StartEdit{
        void onAuthorTextClick();
        void onDescriptionTextClick();
    }
}
