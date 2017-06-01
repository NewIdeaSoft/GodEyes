package com.nisoft.inspectortools.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Content;
import com.nisoft.inspectortools.ui.base.DatePickerDialog;
import com.nisoft.inspectortools.ui.typeproblem.ProblemRecodeActivity;
import com.nisoft.inspectortools.utils.StringFormatUtil;

import java.util.ArrayList;

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
    public void onBindViewHolder(ProblemInfoItemViewHolder holder, int position) {
        final int witch = position;
        final Content content = mContents.get(position);
        holder.itemTitle.setText(content.getmTitle());
        holder.itemContent.setText(content.getmText());
        holder.itemContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content.setmText(s.toString());
            }
        });
        holder.itemDate.setText(StringFormatUtil.dateFormat(content.getmDate()));
        holder.itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动选择日期对话框
                FragmentManager fm =  ((ProblemRecodeActivity)mContext).getFragmentManager();
                DatePickerDialog dialog = DatePickerDialog.newInstance(content.getmTitle());
                dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_content), 0);
                dialog.show(fm, "date");
            }
        });
        holder.itemAuthor.setText(content.getmAuthor());
        holder.itemAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content.setmAuthor(s.toString());
            }
        });
        holder.itemAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从联系人选择
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                ((Activity)mContext).getFragmentManager().findFragmentById(R.id.fragment_content).startActivityForResult(intent,4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }
    class ProblemInfoItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle;
        EditText itemContent;
        TextView itemDate;
        TextView itemAuthor;
        public ProblemInfoItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.problem_info_item_title);
            itemContent = (EditText) itemView.findViewById(R.id.problem_info_item_content);
            itemDate = (TextView) itemView.findViewById(R.id.problem_info_item_time);
            itemAuthor = (TextView) itemView.findViewById(R.id.problem_info_item_author);
        }
    }

    public void setContents(ArrayList<Content> contents) {
        mContents = contents;
    }

}
