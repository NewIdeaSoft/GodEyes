package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.PicsAdapter;
import com.nisoft.inspectortools.adapter.ProblemInfoItemAdapter;
import com.nisoft.inspectortools.bean.problem.Content;
import com.nisoft.inspectortools.bean.problem.Problem;
import com.nisoft.inspectortools.bean.problem.ProblemLab;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema.ProblemTable;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.ui.base.TimePickerDialog;
import com.nisoft.inspectortools.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.nisoft.inspectortools.bean.problem.Content.getContentsFromProblem;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemRecodeFragment extends Fragment {
    private static Problem mProblem;
    private TextView mTitleEdit;
    private RecyclerView mProblemImageRecycler;
    private PicsAdapter mPicsAdapter;
    private ArrayList<String> mPicsPath;
    private RecyclerView mProblemContentRecycler;
    private ProblemInfoItemAdapter mInfoItemAdapter;
    private ArrayList<Content> mProblemContents;
    private String mRootPath;

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
        if (mProblem.getTitle()!=null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mProblem.getTitle());
        }else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("新增记录");
        }
        setHasOptionsMenu(true);
        mTitleEdit = (TextView) view.findViewById(R.id.problem_title_edit);
        mTitleEdit.setText(mProblem.getTitle());
        mTitleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditTextActivity.class);
                intent.putExtra("initText",mTitleEdit.getText());
                startActivityForResult(intent,4);
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
        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/工作相册/质量问题/"+mProblem.getTitle()+"_"+mProblem.get_id()+"/";
        mPicsAdapter = new PicsAdapter(this,mPicsPath,R.layout.problem_image_item,mRootPath);
        mProblemContents = getContentsFromProblem(mProblem);
        mInfoItemAdapter = new ProblemInfoItemAdapter(mProblemContents,getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mProblem.getTitle()!=null) {
            Content.getProblem(mProblemContents,mProblem);
            ProblemLab.getProblemLab(getActivity()).updateProblem(mProblem);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPicsAdapter.notifyDataSetChanged();
        mInfoItemAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recode_toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.data_upload:
//                File dir = new File(mRootPath);
//                if (dir.exists()&&dir.listFiles().length>0){
//                    ProgressDialog dialog = new ProgressDialog(getActivity());
//                    FileUploadUtil.uploadFileDir(dir);
//                }else {
//                    Toast.makeText(getActivity(), "没有可上传的文件!", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.data_push:
                String data = "";
                for (Content content: mProblemContents){
                    data+=content.toString();
                }
                //需在分线程执行，显示进度条
                File file = new File(mRootPath);
                if(!file.exists()) {
                    file.mkdir();
                }
                FileUtil.writeStringToFile(data,mRootPath+mProblem.getTitle()+".txt");
                Toast.makeText(getActivity(),"数据导出",Toast.LENGTH_SHORT).show();
                break;
            case R.id.data_share:
                String data1 = "";
                for (Content content: mProblemContents){
                    data1+=content.toString();
                }
                Intent i  = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,data1);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
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
                int contentPosition = data.getIntExtra(TimePickerDialog.CLICK_POSITION,-1);
                if(contentPosition!=-1) {
                    mProblemContents.get(contentPosition).setmDate(date);
                    mInfoItemAdapter.setContents(mProblemContents);
                }
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


                }else if(path==null&&resourcePhotoPath!=null){
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
//                    mInfoItemAdapter.notifyDataSetChanged();
                }
                break;
            case 3:
                String content = data.getStringExtra("content_edit");
                int contentPosition1 = data.getIntExtra("content_position",-1);
                if (contentPosition1>-1){
                    mProblemContents.get(contentPosition1).setmText(content);
                    mInfoItemAdapter.setContents(mProblemContents);
//                    mInfoItemAdapter.notifyDataSetChanged();
                }
                break;
            case 4:
                String content1 = data.getStringExtra("content_edit");
                mProblem.setTitle(content1);
                mTitleEdit.setText(content1);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mProblem.getTitle());
                break;
        }
    }

    public static Problem getProblem() {
        if (mProblem == null) {
            mProblem = new Problem();
        }
        return mProblem;
    }
    public void removeSelectedPic(int position){
        File file = new File(mPicsPath.get(position));
        file.delete();
        mPicsPath.remove(position);
        mProblem.setPhotoPath(mPicsPath);
        mPicsAdapter.setPicsPath(mPicsPath);
        mPicsAdapter.notifyDataSetChanged();
    }
}
