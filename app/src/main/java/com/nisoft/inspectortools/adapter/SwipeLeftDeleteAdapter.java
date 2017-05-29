package com.nisoft.inspectortools.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.Problem;
import com.nisoft.inspectortools.bean.problem.ProblemLab;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema;
import com.nisoft.inspectortools.ui.typeproblem.ProblemListActivity;
import com.nisoft.inspectortools.ui.typeproblem.ProblemRecodeActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SwipeLeftDeleteAdapter extends RecyclerView.Adapter<SwipeLeftDeleteAdapter.SwipeLeftViewHolder> {
    private Context mContext;
    private Problem mProblem;
    public SwipeLeftDeleteAdapter(Context context, Problem problem){
        mContext = context;
        mProblem = problem;
    }
    @Override
    public SwipeLeftViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.problems_list_item,parent,false);
        return new SwipeLeftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SwipeLeftViewHolder holder, int position) {
        if (position == 0){
            if (mProblem.getPhotoPath()!=null&&mProblem.getPhotoPath().size()>0){
                Glide.with(mContext).load(mProblem.getPhotoPath().get(0)).into(holder.mProblemImageView);
            }
            if (mProblem.getTitle()!=null){
                holder.mProblemTitle.setText(mProblem.getTitle());
            }
            if (mProblem.getDetailedText()!=null){
                holder.mProblemDetailedInfo.setText(mProblem.getDetailedText());
            }

            holder.mParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProblemRecodeActivity.class);
                    intent.putExtra(ProblemDbSchema.ProblemTable.Cols.UUID,mProblem.getUUID());
                    mContext.startActivity(intent);
                }
            });

        }else if (position == 1){
            holder.mCardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            holder.mParent.removeAllViews();
            TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.delete_problem,holder.mParent,false);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> photosPath = mProblem.getPhotoPath();
                    ProblemLab.getProblemLab(mContext.getApplicationContext()).delete(mProblem);
                    //删除照片文件，添加从相册复制文件到应用图片存储文件后启用，以免删除相册图片

                    if (photosPath!=null&&photosPath.size()>0){
                        for (int i = 0;i<photosPath.size();i++){
                            File file = new File(photosPath.get(i));
                            file.delete();
                        }
                    }
                    (((ProblemListActivity)mContext).getFragmentManager().findFragmentById(R.id.fragment_content)).onResume();
                }
            });
            holder.mParent.addView(textView);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    static class SwipeLeftViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mParent;
        CardView mCardView;
        ImageView mProblemImageView;
        TextView mProblemTitle;
        TextView mProblemDetailedInfo;
        public SwipeLeftViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            mParent = (LinearLayout) itemView.findViewById(R.id.item_parent_view);
            mProblemImageView = (ImageView) itemView.findViewById(R.id.problem_first_picture);
            mProblemTitle = (TextView) itemView.findViewById(R.id.text_problem_title);
            mProblemDetailedInfo = (TextView) itemView.findViewById(R.id.text_problem_simple_info);
        }
    }
}
