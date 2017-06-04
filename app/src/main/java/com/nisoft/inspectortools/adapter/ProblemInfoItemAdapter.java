package com.nisoft.inspectortools.adapter;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Content;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.typeproblem.EditModeDialog;
import com.nisoft.inspectortools.ui.base.EditTextActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemRecodeActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemRecodeFragment;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ProblemInfoItemAdapter extends RecyclerView.Adapter<ProblemInfoItemAdapter.ProblemInfoItemViewHolder>{
    private Context mContext;
    private ArrayList<Content> mContents;

    public ProblemInfoItemAdapter(ArrayList<Content> contents, Context context) {
        this.mContext = context;
        mContents = contents;
    }

    @Override
    public ProblemInfoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.problem_info_item,parent,false);
        return new ProblemInfoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProblemInfoItemViewHolder holder, final int position) {
        final Content content = mContents.get(position);
        Log.e("Content",content.toString());
        holder.itemTitle.setText(content.getmTitle());
        holder.itemContent.setText(content.getmText());
        holder.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditTextActivity.class);
                intent.putExtra("content_position",position);
                intent.putExtra("initText",holder.itemContent.getText().toString());
                (((ProblemRecodeActivity)mContext).getFragmentManager().findFragmentById(R.id.fragment_content))
                        .startActivityForResult(intent,3);
            }
        });
        final Date date = content.getmDate();
        if(date.getTime()!=0L) {
            holder.itemDate.setText(StringFormatUtil.dateFormat(date));
        }
        holder.itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动选择日期对话框
                FragmentManager fm =  ((ProblemRecodeActivity)mContext).getFragmentManager();
                DatePickerDialog dialog;
                if(date.getTime()!=0L) {
                    dialog = DatePickerDialog.newInstance(position,date);
                }else {
                    dialog = DatePickerDialog.newInstance(position,new Date());
                }

                dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_content), 0);
                dialog.show(fm, "date");
            }
        });
        holder.itemAuthor.setText(content.getmAuthor());
        holder.itemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((ProblemRecodeActivity)mContext).getFragmentManager();
                ProblemRecodeFragment targetFragment = (ProblemRecodeFragment) fm.findFragmentById(R.id.fragment_content);
                DialogFragment dialogFragment = EditModeDialog.newInstance(position,holder.itemAuthor.getText().toString());
                dialogFragment.setTargetFragment(targetFragment,2);
                dialogFragment.show(fm,"edit_mode");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }
    class ProblemInfoItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle;
        TextView itemContent;
        TextView itemDate;
        TextView itemAuthor;
        public ProblemInfoItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.problem_info_item_title);
            itemContent = (TextView) itemView.findViewById(R.id.problem_info_item_content);
            itemDate = (TextView) itemView.findViewById(R.id.problem_info_item_time);
            itemAuthor = (TextView) itemView.findViewById(R.id.problem_info_item_author);
        }
    }
    public void setContents(ArrayList<Content> contents) {
        mContents = contents;
    }

}
