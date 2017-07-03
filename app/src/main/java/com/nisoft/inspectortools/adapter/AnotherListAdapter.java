package com.nisoft.inspectortools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/5/18.
 */

public class AnotherListAdapter extends RecyclerView.Adapter<AnotherListAdapter.ProblemViewHolder> {
    private ArrayList<ProblemRecode> mProblems;
    private Context mContext;
    public AnotherListAdapter(Context context, ArrayList<ProblemRecode> problems){
        mContext = context;
        mProblems = problems;
    }
    @Override
    public ProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_new,parent,false);
        return new ProblemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProblemViewHolder holder, int position) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        holder.mRecyclerView.setLayoutManager(layoutManager);
        SwipeLeftDeleteAdapter adapter = new SwipeLeftDeleteAdapter(mContext,mProblems.get(position));
        holder.mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mProblems.size();
    }

    static class ProblemViewHolder extends RecyclerView.ViewHolder{
        RecyclerView mRecyclerView;
        public ProblemViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.problem_list_item);
        }
    }

    public void setProblems(ArrayList<ProblemRecode> mProblems) {
        this.mProblems = mProblems;
    }
}
