package com.nisoft.inspectortools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Content;
import com.nisoft.inspectortools.bean.problem.Problem;

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
        Content content = mContents.get(position);
        holder.itemTitle.setText(content.getmTitle());
        holder.itemContent.setText(content.getmText());
        holder.itemDate.setText(content.getmDate().toString());
        holder.itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动选择日期对话框
            }
        });
        holder.itemAuthor.setText(content.getmAuthor());
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    class ProblemInfoItemViewHolder extends RecyclerView.ViewHolder{
        TextView itemTitle;
        EditText itemContent;
        Button itemDate;
        Button itemAuthor;
        public ProblemInfoItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.problem_info_item_title);
            itemContent = (EditText) itemView.findViewById(R.id.problem_info_item_content);
            itemDate = (Button) itemView.findViewById(R.id.problem_info_item_time);
            itemAuthor = (Button) itemView.findViewById(R.id.problem_info_item_author);
        }
    }

    public void setContents(ArrayList<Content> contents) {
        mContents = contents;
    }

    public ArrayList<Content> getContents(){
        for (int i = 0;i<mContents.size();i++){
//            mContents.get(i).setmText();
        }
        return mContents;
    }
}
