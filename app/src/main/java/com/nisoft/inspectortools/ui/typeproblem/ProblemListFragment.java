package com.nisoft.inspectortools.ui.typeproblem;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nisoft.inspectortools.R;
import com.nisoft.inspectortools.adapter.AnotherListAdapter;
import com.nisoft.inspectortools.bean.problem.ProblemDataLab;
import com.nisoft.inspectortools.bean.problem.ProblemDataPackage;
import com.nisoft.inspectortools.bean.problem.ProblemRecode;
import com.nisoft.inspectortools.db.problem.RecodeCursorWrapper;
import com.nisoft.inspectortools.db.problem.RecodeDbSchema;
import com.nisoft.inspectortools.gson.ProblemListDataPackage;
import com.nisoft.inspectortools.utils.DialogUtil;
import com.nisoft.inspectortools.utils.GsonUtil;
import com.nisoft.inspectortools.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private ProgressDialog mDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProblems = ProblemDataLab.getProblemDataLab(getActivity()).getAllProblem();
        mAdapter = new AnotherListAdapter(getActivity(), mProblems);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problems_list, container, false);
        setHasOptionsMenu(true);
        ActionBar actionBar =
                ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("问题记录");
        }

        mDialog = new ProgressDialog(getActivity());
        mProblemsRecyclerView = (RecyclerView) view.findViewById(R.id.problems_list_recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mProblemsRecyclerView.setLayoutManager(manager);
        mProblemsRecyclerView.setAdapter(mAdapter);
        mNewProblemRecodeFAB = (FloatingActionButton) view.findViewById(R.id.new_problem_fab);
        mNewProblemRecodeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProblem();
            }
        });
        refreshFromServer();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
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
                        .queryRecode(RecodeDbSchema.RecodeTable.PROBLEM_NAME, RecodeDbSchema.RecodeTable.Cols.TITLE + " like ?"
                                , new String[]{"%" + newText + "%"});
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
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
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
        RequestBody body = new FormBody.Builder()
                .add("intent", "list")
                .build();
        DialogUtil.showProgressDialog(getActivity(), mDialog, "正在同步列表...");
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_PROBLEM_RECODE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Toast.makeText(getActivity(), "连接网络失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("listJson:", result);
                if (!result.equals("zero")) {
                    Gson gson = GsonUtil.getDateFormatGson();
                    ProblemListDataPackage recodeData = gson.fromJson(result, ProblemListDataPackage.class);
                    ArrayList<ProblemRecode> recodes = recodeData.getProblemRecodes();
                    for (ProblemRecode recode1 : recodes) {
                        for (ProblemRecode recode2 : mProblems) {
                            if (recode1.getRecodeId().equals(recode2.getRecodeId())) {
                                if (recode1.getUpdateTime() > recode2.getUpdateTime()) {
                                    ProblemDataLab.getProblemDataLab(getActivity())
                                            .updateRecode(RecodeDbSchema.RecodeTable.PROBLEM_NAME, recode1);
                                } else {
                                    recodes.set(recodes.indexOf(recode1), recode2);
                                }
                                break;
                            }
                            if (mProblems.indexOf(recode2) == mProblems.size() - 1) {
                                ProblemDataLab.getProblemDataLab(getActivity())
                                        .updateRecode(RecodeDbSchema.RecodeTable.PROBLEM_NAME, recode1);
                            }
                        }
                    }
                    mProblems = recodes;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        mAdapter.setProblems(mProblems);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "同步完成！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSearchAutoComplete != null && mSearchAutoComplete.isShown()) {
            mSearchAutoComplete.setText("");
        }
        if (mAdapter != null) {
            ArrayList<ProblemRecode> problems = ProblemDataLab.getProblemDataLab(getActivity()).getAllProblem();
            mAdapter.setProblems(problems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void createProblem() {
        RequestBody body = new FormBody.Builder()
                .add("intent", "new_problem")
                .build();
        HttpUtil.sendPostRequest(HttpUtil.SERVLET_PROBLEM_RECODE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("false")) {
                            Toast.makeText(getActivity(), "新建记录失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            ProblemDataPackage problem = new ProblemDataPackage(result);
                            Intent intent = new Intent(getActivity(), ProblemRecodeActivity.class);
                            ProblemDataLab.getProblemDataLab(getActivity()).updateProblem(problem);
                            intent.putExtra(RecodeDbSchema.RecodeTable.Cols.PROBLEM_ID, result);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }

    private void changeAdapterData(RecodeCursorWrapper cursor) {
        ArrayList<ProblemRecode> problems = ProblemDataLab.getProblemDataLab(getActivity()).getProblems(cursor);
        mAdapter.setProblems(problems);
        mAdapter.notifyDataSetChanged();
    }

}
