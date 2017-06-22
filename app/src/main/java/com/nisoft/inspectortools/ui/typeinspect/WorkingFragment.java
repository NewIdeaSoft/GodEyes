package com.nisoft.inspectortools.ui.typeinspect;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.PicsAdapter;
import com.nisoft.inspectortools.bean.inspect.InspectRecodePics;
import com.nisoft.inspectortools.bean.inspect.PicsLab;
import com.nisoft.inspectortools.db.inspect.PicsDbSchema;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.choosetype.ChooseRecodeTypeFragment;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.utils.FileUtil;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Administrator on 2017/5/22.
 */

public class WorkingFragment extends Fragment {
    private TextView mDatePickerButton;
    private EditText mJobNumber;
    private TextView mDescriptionText;
    private RecyclerView mPicsView;
    private static InspectRecodePics sRecodePics;
    private ArrayList<String> mPicsPath;
    private PicsAdapter mAdapter;
    private ImageButton mJobNumSaveButton;
    private String oldJobNum;
    private String jobType;
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/工作相册/";

    public static WorkingFragment newInstance(String jobNum,String inspectType) {
        WorkingFragment fragment = new WorkingFragment();
        Bundle args = new Bundle();
        if (jobNum != null) {
            args.putString("job_num", jobNum);
        }
        args.putString(ChooseRecodeTypeFragment.INSPECT_TYPE,inspectType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        String selection = getArguments().getString("job_num");
        if (selection != null) {
            sRecodePics = PicsLab.getPicsLab(getActivity()).getPicsByJobNum(selection);
            oldJobNum = sRecodePics.getJobNum();
        } else {
            sRecodePics = new InspectRecodePics();
        }
        mPicsPath = sRecodePics.getPicPath();
        if (mPicsPath == null) {
            mPicsPath = new ArrayList<>();
        }
        jobType = getArguments().getString(ChooseRecodeTypeFragment.INSPECT_TYPE);
        String path = PATH+jobType+"/";
        mAdapter = new PicsAdapter(WorkingFragment.this,mPicsPath,R.layout.inspect_image_item,path);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.working_fragment, container, false);
        if (oldJobNum!=null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(oldJobNum);
        }else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("New Job");
        }
        setHasOptionsMenu(true);
        mPicsView = (RecyclerView) view.findViewById(R.id.pics_list);
        mJobNumber = (EditText) view.findViewById(R.id.job_num_edit);
        mDatePickerButton = (TextView) view.findViewById(R.id.date_picker_button);
        mJobNumSaveButton = (ImageButton) view.findViewById(R.id.job_num_save);
        mDescriptionText = (TextView) view.findViewById(R.id.job_description);
        if(sRecodePics.getDescription()!=null){
            mDescriptionText.setText(sRecodePics.getDescription());
        }
        Date date;
        if (sRecodePics.getDate() != null) {
            date = sRecodePics.getDate();
        } else {
            date = new Date();
            sRecodePics.setDate(date);
        }
        mDatePickerButton.setText(StringFormatUtil.dateFormat(date));
        if (oldJobNum != null) {
            mJobNumber.setText(oldJobNum);
            setEditable(false);
        } else {
            String s = null;
            if (jobType.equals("原材料检验")){
                s = mDatePickerButton.getText().toString().substring(0, 5);
            } else if (jobType.equals("外购件检验")){
                s = mDatePickerButton.getText().toString().substring(0, 8);
            }
            mJobNumber.setText(s);
            mJobNumber.setSelection(s.length());
        }

        mJobNumSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s1 = mJobNumber.getText().toString();
                if (mJobNumber.isFocusable()){
                    if (s1.length() >=6) {
                        if (oldJobNum == null) {
                            oldJobNum = s1;
                            if (!handNewJob(s1, "您输入的编号已存在，请重新输入！")){
                                oldJobNum = null;
                            }
                        } else {
                            if (!s1.equals(oldJobNum)) {
                                Snackbar.make(mJobNumSaveButton, "确定更改原检验编号吗？", Snackbar.LENGTH_SHORT)
                                        .setAction("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(handNewJob(s1, "您输入的编号已存在，请重新输入！")){
                                                    oldJobNum = s1;
                                                    setEditable(false);
                                                }
                                            }
                                        }).show();
                            }else {
                                setEditable(false);
                            }
                        }
                    }else {
                        Toast.makeText(getActivity(), "您还没有输入检验编号！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    setEditable(true);
                }
            }
        });
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(0, sRecodePics.getDate());
            }
        });
        mDescriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditTextActivity.class);
                intent.putExtra("initText",mDescriptionText.getText());
                startActivityForResult(intent,2);
            }
        });
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPicsView.setLayoutManager(manager);
        mPicsView.setAdapter(mAdapter);
        return view;
    }

    private void setEditable(boolean editable){
        mJobNumber.setFocusable(editable);
        mJobNumber.setFocusableInTouchMode(editable);
        mJobNumber.setEnabled(editable);
        if (editable) {
            mJobNumSaveButton.setImageResource(R.drawable.btn_done);
            mJobNumber.setSelection(mJobNumber.getText().toString().length());

        }else{
            mJobNumSaveButton.setImageResource(R.drawable.btn_edit);
        }

    }

    private boolean isExitJobNum(String s) {
        return PicsLab.getPicsLab(getActivity()).queryPics(PicsDbSchema.PicTable.Cols.PIC_JOB_NUM + "=?", new String[]{s}).getCount() > 0;
    }

    private boolean handNewJob(String newJobNum,String toastText){
        if (isExitJobNum(newJobNum)) {
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            sRecodePics.setJobNum(newJobNum);
            PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics,oldJobNum);
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recode_toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.data_push:
                //将实体 格式化为字符串
                String data = sRecodePics.toString();
                //在分线程写入字符串到指定目录的文件下
                String path = PATH+jobType+"/"+sRecodePics.getJobNum()+"/";
                File file = new File(path);
                if(!file.exists()) {
                    file.mkdirs();
                }
                FileUtil.writeStringToFile(data,path+sRecodePics.getJobNum()+".txt");
                Toast.makeText(getActivity(), "导出数据完成！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.data_share:
                Intent i  = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,sRecodePics.toString());
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatePickerButton.setText(StringFormatUtil.dateFormat(sRecodePics.getDate()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        sRecodePics.setType(jobType);
        PicsLab.getPicsLab(getActivity()).updatePics(sRecodePics,sRecodePics.getJobNum());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 0:
                Date date = (Date) data.getSerializableExtra(DatePickerDialog.DATE_INITIALIZE);
                sRecodePics.setDate(date);
                mDatePickerButton.setText(StringFormatUtil.dateFormat(sRecodePics.getDate()));
                break;
            case 1:
                final String path = data.getStringExtra("PhotoPath");
                final String resourcePhotoPath = data.getStringExtra("resourcePhotoPath");
                final int position = data.getIntExtra("position",-1);
                if (path != null&&position>-1) {
//                    mAdapter.refreshPics();

                    if (position<mPicsPath.size()){
                        mPicsPath.set(position,path);
                    }else {
                        mPicsPath.add(path);
                    }
                    if(resourcePhotoPath!=null){
                        Snackbar.make(mPicsView,"已移动照片至"+path+",点击撤销",Snackbar.LENGTH_LONG)
                                .setAction("撤销", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FileUtil.moveFile(path,resourcePhotoPath);
//                                        mAdapter.refreshPics();
                                        mPicsPath.remove(position);

                                        mAdapter.notifyDataSetChanged();
                                    }
                                })
                                .show();
                    }


                }else if(path==null){
                    Toast.makeText(getActivity(),"照片已存在，请重新选择！",Toast.LENGTH_LONG).show();
                }
                //mAdapter.refreshPics();
                mAdapter.setPicsPath(mPicsPath);
                sRecodePics.setPicPath(mPicsPath);
                break;
            case 2:
                String text = data.getStringExtra("content_edit");
                sRecodePics.setDescription(text);
                mDescriptionText.setText(text);
                break;
        }
    }

    public static InspectRecodePics getsRecodePics() {
        if (sRecodePics == null) {
            sRecodePics = new InspectRecodePics();
        }
        return sRecodePics;
    }


    private void showDatePickerDialog(int requestCode, Date date) {
        FragmentManager fm = getFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(-1,date);
        dialog.setTargetFragment(WorkingFragment.this, requestCode);
        dialog.show(fm, "date");
    }
    public EditText getJobNumber(){
        return mJobNumber;
    }
    public void removeSelectedPic(int position){
        File file = new File(mPicsPath.get(position));
        file.delete();
        //mAdapter.refreshPics();
        mPicsPath.remove(position);
        mAdapter.setPicsPath(mPicsPath);
        sRecodePics.setPicPath(mPicsPath);

        mAdapter.notifyDataSetChanged();
    }
}

