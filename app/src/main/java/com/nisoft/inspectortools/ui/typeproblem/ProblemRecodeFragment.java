package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.utils.FileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.nisoft.inspectortools.bean.problem.Content.CONTENT_TITLES;
import static com.nisoft.inspectortools.bean.problem.Content.getContentsFromProblem;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment extends Fragment {
    private static Problem mProblem;
    private EditText mTitleEdit;
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
        mTitleEdit = (EditText) view.findViewById(R.id.problem_title_edit);
        mTitleEdit.setText(mProblem.getTitle());
        if(mProblem.getTitle()!=null) {
            mTitleEdit.setFocusable(false);
            mTitleEdit.setFocusableInTouchMode(false);
        }
        mTitleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mProblem.setTitle(s.toString());
            }
        });
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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/工作相册/质量问题/"+uuid.toString()+"/";
        mPicsAdapter = new PicsAdapter(this,mPicsPath,R.layout.problem_image_item,path);
        mProblemContents = getContentsFromProblem(mProblem);
        mInfoItemAdapter = new ProblemInfoItemAdapter(mProblemContents,getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        Content.getProblem(mProblemContents,mProblem);
        ProblemLab.getProblemLab(getActivity()).updateProblem(mProblem);
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
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                String contentTitle = data.getStringExtra(DatePickerDialog.REQUEST_TITLE);
                int contentPosition = -1;
                if(contentTitle.equals(CONTENT_TITLES[0])) {
                    contentPosition=0;
                }else if(contentTitle.equals(CONTENT_TITLES[1])) {
                    contentPosition=1;
                }else if(contentTitle.equals(CONTENT_TITLES[2])) {
                    contentPosition=2;
                }
                mProblemContents.get(contentPosition).setmDate(date);
                mInfoItemAdapter.setContents(mProblemContents);
                mInfoItemAdapter.notifyDataSetChanged();
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
            case 2:
                String author = data.getStringExtra("AuthorName");
                int authorPosition = data.getIntExtra("Content position",-1);
                if (authorPosition>-1){
                    mProblemContents.get(authorPosition).setmAuthor(author);
                    mInfoItemAdapter.setContents(mProblemContents);
                    mInfoItemAdapter.notifyDataSetChanged();
                }
                break;
            case 3:
                String content = data.getStringExtra("content_edit");
                int contentPosition1 = data.getIntExtra("content_position",-1);
                if (contentPosition1>-1){
                    mProblemContents.get(contentPosition1).setmText(content);
                    mInfoItemAdapter.setContents(mProblemContents);
                    mInfoItemAdapter.notifyDataSetChanged();
                }
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
