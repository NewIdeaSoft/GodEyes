package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.AnotherListAdapter;
import com.nisoft.inspectortools.bean.problem.Problem;
import com.nisoft.inspectortools.bean.problem.ProblemLab;
import com.nisoft.inspectortools.db.problem.ProblemDbSchema;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemListFragment extends Fragment {
    private ArrayList<Problem> mProblems;
//    private ProblemsListAdapter mAdapter;
    private AnotherListAdapter mAdapter;
    private RecyclerView mProblemsRecyclerView;
    private FloatingActionButton mNewProblemRecodeFAB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProblems = ProblemLab.getProblemLab(getActivity()).getProblems();
        Log.e("length1:",mProblems.size()+"");
//        mAdapter = new ProblemsListAdapter(this,mProblems);
        mAdapter = new AnotherListAdapter(getActivity(),mProblems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems_list,container,false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.problem_list_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        mProblemsRecyclerView = (RecyclerView) view.findViewById(R.id.problems_list_recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mProblemsRecyclerView.setLayoutManager(manager);
        mProblemsRecyclerView.setAdapter(mAdapter);
        mNewProblemRecodeFAB = (FloatingActionButton) view.findViewById(R.id.new_problem_fab);
        mNewProblemRecodeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProblem();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.toolbar,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_problem_menu :
                createProblem();
                break;
            case R.id.search :

                break;
            case R.id.settings :

                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter == null) {
//            mAdapter = new ProblemsListAdapter(this,mProblems);
            mAdapter = new AnotherListAdapter(getActivity(),mProblems);
            mProblemsRecyclerView.setAdapter(mAdapter);
        }else{
            ArrayList<Problem> problems = ProblemLab.getProblemLab(getActivity()).getProblems();
            mAdapter.setProblems(problems);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void createProblem(){
        Problem problem = new Problem();
        Intent intent = new Intent(getActivity(), ProblemRecodeActivity.class);
        ProblemLab.getProblemLab(getActivity()).addProblem(problem);
        intent.putExtra(ProblemDbSchema.ProblemTable.Cols.UUID,problem.getUUID());
        startActivity(intent);
    }
}
