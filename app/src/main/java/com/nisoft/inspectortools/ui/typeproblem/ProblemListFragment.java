package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.AnotherListAdapter;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.db.problem.RecodeCursorWrapper;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema;

import java.util.ArrayList;

/**
 * Created by NewIdeaSoft on 2017/4/25.
 */

public class ProblemListFragment extends Fragment {
    private ArrayList<ProblemRecode> mProblems;
    private AnotherListAdapter mAdapter;
    private RecyclerView mProblemsRecyclerView;
    private FloatingActionButton mNewProblemRecodeFAB;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProblems = ProblemDataLab.getProblemDataLab(getActivity()).getAllProblem();
        mAdapter = new AnotherListAdapter(getActivity(),mProblems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems_list,container,false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("质量问题");
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
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setQueryHint("搜索质量问题");
        mSearchAutoComplete
                = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchAutoComplete.setThreshold(1);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RecodeCursorWrapper cursor = ProblemDataLab.getProblemDataLab(getActivity())
                        .queryRecode(RecodeDbSchema.RecodeTable.PROBLEM_NAME,RecodeDbSchema.RecodeTable.Cols.TITLE+" like ?"
                                ,new String[]{"%"+newText+"%"});
                changeAdapterData(cursor);
                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                refreshFromServer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * 连接服务器，更新数据，更新列表
     */
    private void refreshFromServer() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter == null) {
            mAdapter = new AnotherListAdapter(getActivity(),mProblems);
            mProblemsRecyclerView.setAdapter(mAdapter);
        }else{
            if(mSearchAutoComplete!=null&&mSearchAutoComplete.isShown()) {
                mSearchAutoComplete.setText("");
            }
            ArrayList<ProblemRecode> problems = ProblemDataLab.getProblemDataLab(getActivity()).getAllProblem();
            mAdapter.setProblems(problems);
            mAdapter.notifyDataSetChanged();
        }
    }
    private void createProblem(){
        ProblemRecode problem = new ProblemRecode();
        Intent intent = new Intent(getActivity(), ProblemRecodeActivity.class);
        ProblemDataLab.getProblemDataLab(getActivity()).addProblem(problem);
        intent.putExtra(RecodeDbSchema.RecodeTable.Cols.PROBLEM_ID,problem.getRecodeId());
        startActivity(intent);
    }

    private void changeAdapterData(RecodeCursorWrapper cursor){
        ArrayList<ProblemRecode> problems = ProblemDataLab.getProblemDataLab(getActivity()).getProblems(cursor);
        mAdapter.setProblems(problems);
        mAdapter.notifyDataSetChanged();
    }
}
